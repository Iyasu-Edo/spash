package it.nerdammer.spash.shell;

import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.*;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows creating {@code SshServer} instances.
 *
 * @author Nicola Ferraro
 */
public class SshServerFactory {


    public static SshServer create() {

        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(2222);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File("key.ser")));

        List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
        userAuthFactories.add(new UserAuthPasswordFactory());
        sshd.setUserAuthFactories(userAuthFactories);

        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
            public boolean authenticate(String username, String password, ServerSession session) {
                return username!=null && username.length()>0 && username.equals(password);
            }
        });

        sshd.setShellFactory(new SpashShellFactory());
        //sshd.setCommandFactory(new ScpCommandFactory());

        return sshd;
    }

}


