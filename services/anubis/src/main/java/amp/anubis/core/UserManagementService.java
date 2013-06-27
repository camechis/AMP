package amp.anubis.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.eventing.IEventBus;

public class UserManagementService {

    private static Logger logger = LoggerFactory.getLogger(UserManagementService.class);

    private IEventBus _eventBus;


    public UserManagementService(IEventBus eventBus) {

        _eventBus = eventBus;
    }


    public void start() {

        logger.debug("Starting the User Management Service.");
    }

    public void stop() {

        logger.debug("Stopping the User Management Service.");

        _eventBus.dispose();
    }
}
