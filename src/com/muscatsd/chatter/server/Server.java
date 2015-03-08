package com.muscatsd.chatter.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.muscatsd.chatter.server.enumeration.ServerStatus;
import com.muscatsd.chatter.server.frame.ServerMainFrame;

/**
 * Represent the server application.
 * @author Hatem Al Amri
 */
public class Server {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	// server attributes
	private String serverName;
	private Integer serverPort;
	private ServerStatus serverStatus = ServerStatus.STOPPED;
	private ServerSocket socket;
	private ServerSession serverSession;
	
	// server frame
	private ServerMainFrame serverMainFrame = new ServerMainFrame() ;
	
	// constants
	private final String DEFAULT_SERVER_NAME = "Chatter";
	private final Integer DEFAULT_SERVER_PORT = 5555;
	
	
	/**
	 * Default constructor.
	 */
	public Server(){
		
		setServerName(DEFAULT_SERVER_NAME);
		setServerPort(DEFAULT_SERVER_PORT);
	}
	
	/**
	 * Constructor with server name and server port.
	 * @param serverName Server name
	 * @param serverPort Server port
	 */
	public Server(String serverName,Integer serverPort){
		
		setServerName(serverName);
		setServerPort(serverPort);

	}
	
	/**
	 * Server application driver.
	 */
	public static void main(String[] args) {
		Server server = new Server();
		server.getServerMainFrame().setServer(server);
		server.getServerMainFrame().run();

	}
	
	/**
	 * Start server.
	 * @return Boolean true if success.
	 */
	public Boolean start(){
		try {
			// prepare server socket and establish it
			ServerSocket serverSocket = new ServerSocket(getServerPort());
			setSocket(serverSocket);
			
			// prepare server session and start it
			ServerSession serverSession = new ServerSession();
			setServerSession(serverSession);
			getServerSession().setServerSocket(getSocket());
			getServerSession().setServerMainFrame(getServerMainFrame());
			getServerSession().start();
			
			// change server status to started
			setServerStatus(ServerStatus.STARTED);
			
			// success start of server
			return true;
			
		} catch (IOException e) {
			
			// port is used by another app
			JOptionPane.showMessageDialog(null, "Port " + getServerPort() + " is used by another application.", "Error",JOptionPane.ERROR_MESSAGE);
			
		}catch (IllegalArgumentException e){
			
			// server port is out of range
			JOptionPane.showMessageDialog(null, "Port " + getServerPort() + " is not out of the range.", "Error",JOptionPane.ERROR_MESSAGE);
		}
		
		// failed start of server
		return false;

	}
	
	/**
	 * Stop server.
	 * @return Boolean if stop success
	 */
	public Boolean stop(){
		try {
			if(!getSocket().isClosed()){
				
				// stop session and socket
				getSocket().close();
				getServerSession().stopSession();
								
				// change server status to stopped
				setServerStatus(ServerStatus.STOPPED);
				
				// success server stop
				return true;

			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
		}
		
		// fail server stop
		return false;
	}

	/*** Getters & Setters **/
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public ServerMainFrame getServerMainFrame() {
		return serverMainFrame;
	}

	public void setServerMainFrame(ServerMainFrame serverMainFrame) {
		this.serverMainFrame = serverMainFrame;
	}

	public ServerStatus getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(ServerStatus serverStatus) {
		this.serverStatus = serverStatus;
	}

	public ServerSocket getSocket() {
		return socket;
	}

	public void setSocket(ServerSocket socket) {
		this.socket = socket;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public String getDEFAULT_SERVER_NAME() {
		return DEFAULT_SERVER_NAME;
	}

	public Integer getDEFAULT_SERVER_PORT() {
		return DEFAULT_SERVER_PORT;
	}

	public ServerSession getServerSession() {
		return serverSession;
	}

	public void setServerSession(ServerSession serverSession) {
		this.serverSession = serverSession;
	}


}