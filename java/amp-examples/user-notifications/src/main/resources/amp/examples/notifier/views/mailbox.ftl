<#-- @ftlvariable name="" type="amp.examples.notifier.views.MailboxView" -->
<!DOCTYPE html>
<html lang="en-US">
	<header>
		<title>Mailbox of ${userDetails.username}</title>
		<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
		<script type="text/javascript">
			$(function(){
				$("button").click(function(){
					
					var msgId = $(this).attr("msgId");
					
					$.post("/service/notifications", { msgId: msgId })
					.always( 
					function(ctx){
						// Success
						if (ctx.status == 200){
							
							// Remove the row!
							$("#tr_" + msgId).remove();
						}
						// Fail
						else {
						
							alert("Could not send acknowledgment.");
						}
					})
				});
			});
		</script>
	</header>
	<body>
		<h1>Mailbox of ${userDetails.username}</h1>
		<#if hasMail>
			<table style="width: 100%; text-align: center;">
			<tr>
				<th>Time</th>
				<th>From</th>
				<th>Message</th>
				<th></th>
			</tr>
			<#list notifications as notification>
				<tr id="tr_${notification.id}">
					<td>${notification.dateTime}</td>
					<td>${notification.senderUserId}</td>
					<td>${notification.message}</td>
					<td><button type="button" msgId="${notification.id}">Acknowledge</button></td>
				</tr>
			</#list>
			</table>
		<#else>
			<strong>No Messages in Mailbox.</strong>
		</#if>
	</body>
</html>