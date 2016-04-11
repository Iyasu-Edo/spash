package it.nerdammer.spash.shell;

import it.nerdammer.spash.shell.api.fs.SpashFileSystem;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.FileSystemFactory;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
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
        sshd.setPort(SpashConfig.getInstance().spashListenPort());
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File(SpashConfig.getInstance().spashKeyFileName())));

        List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
        userAuthFactories.add(new UserAuthPasswordFactory());
        sshd.setUserAuthFactories(userAuthFactories);

        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
            @Override
            public boolean authenticate(String username, String password, ServerSession serverSession) throws PasswordChangeRequiredException {
                return username!=null && username.length()>0 && username.equals(password);
            }
        });

        sshd.setShellFactory(new SpashShellFactory());

        List<NamedFactory<Command>> namedFactoryList = new ArrayList<>();
        namedFactoryList.add(new SftpSubsystemFactory());
        sshd.setSubsystemFactories(namedFactoryList);

        sshd.setCommandFactory(new ScpCommandFactory());

        sshd.setFileSystemFactory(new FileSystemFactory() {
            @Override
            public FileSystem createFileSystem(Session session) throws IOException {
                return SpashFileSystem.get().getFileSystem();
            }
        });

        return sshd;
    }

}


