package amp.eventing;


import java.security.Signature;
import java.security.cert.X509Certificate;

import amp.bus.security.CredentialHolder;
import amp.bus.security.ICertificateProvider;
import amp.bus.security.IUserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.messaging.EnvelopeHelper;
import amp.messaging.IContinuationCallback;
import amp.messaging.IMessageProcessor;
import amp.messaging.MessageContext;
import amp.messaging.MessageContext.Directions;
import amp.messaging.MessageException;


public class DigitalSignatureProcessor implements IMessageProcessor {

	protected ICertificateProvider certProvider;
	protected IUserInfoRepository userInfoRepo;
	protected CredentialHolder credentials;
	protected Logger log;
	
	
	public DigitalSignatureProcessor(ICertificateProvider certProvider, IUserInfoRepository userInfoRepository) throws Exception {
		this.certProvider = certProvider;
		this.userInfoRepo = userInfoRepository;
		
		this.log = LoggerFactory.getLogger(this.getClass());
		
		try {
			this.credentials = certProvider.getCredentials();
		}
		catch(Exception ex)
		{
			log.error("Failed to get client's PKI credentials", ex);
			throw ex;
		}
	}
	
	
	@Override
	public void processMessage(MessageContext context, IContinuationCallback continuation) throws MessageException {
		
		if (Directions.In == context.getDirection()) {
			this.processInbound(context, continuation);
		}
		else if (Directions.Out == context.getDirection()) {
			this.processOutbound(context, continuation);
		}
	}
	
	public void processOutbound(MessageContext context, IContinuationCallback continuation) throws MessageException {
		
		try {
			EnvelopeHelper env = new EnvelopeHelper(context.getEnvelope());
			
			Signature instance = Signature.getInstance("SHA1withRSA");
			instance.initSign(this.credentials.getPrivateKey());
			instance.update(env.getPayload());			
			
			env.setDigitalSignature(instance.sign());
			
			// make sure that the sender identity has spaces between LDAP elements
			String sender = this.credentials.getCertificate().getSubjectX500Principal().getName();
			sender = sender.replaceAll(",", ", ");
			
			env.setSenderIdentity(sender);
			continuation.continueProcessing();
		}
		catch(Exception ex) {
			log.error("Exception while signing outbound event", ex);
			throw new MessageException("Exception while signing outbound event", ex);
		}
	}

	public void processInbound(MessageContext context, IContinuationCallback continuation) throws MessageException  {

		boolean verified = false;
		
		try {
			EnvelopeHelper env = new EnvelopeHelper(context.getEnvelope());
			String senderIdentity = env.getSenderIdentity();
			
			// remove spaces from the sender identity
			senderIdentity = senderIdentity.replace(", ", ",");
			
			X509Certificate senderCert = this.userInfoRepo.getPublicCertificateFor(senderIdentity);
			
			Signature instance = Signature.getInstance("SHA1withRSA");
			instance.initVerify(senderCert);
			instance.update(env.getPayload());
			
			verified = instance.verify(env.getDigitalSignature());
			
			if (false == verified) {
				log.warn("Event may have been tampered with (id:{})", env.getMessageId().toString());
			}
		}
		catch(Exception ex) {
			log.error("Failed to verify sender's digital signature", ex);
			throw new MessageException("Failed to verify sender's digital signature", ex);
		}
		
		if (verified) { continuation.continueProcessing(); }
	}
}
