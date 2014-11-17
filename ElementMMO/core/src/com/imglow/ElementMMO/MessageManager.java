package com.imglow.ElementMMO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class MessageManager{
	private static MessageManager instance;
	private Vector<Message> messages, queue;
	
	Socket s;
	Runnable input, output;
	Object msgLock = new Object();

	protected MessageManager(){
		messages = new Vector<Message>();
		queue = new Vector<Message>();
	}

	public static MessageManager getInstance() {
		if(instance == null) {
			instance = new MessageManager();
		}
		return instance;
	}
	
	public void init(String host, int port)
	{
		try {
			s = new Socket (host, port);
			
			input = new Runnable(){
				public void run() {
					try {
						ObjectInputStream br= new ObjectInputStream(s.getInputStream());
						int x = 2;
						while(x == 2)
						{
							System.out.println( "Waiting for message" );
							try {
								Message msg = (Message) br.readObject();
								messages.add(msg);
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println("Message Object Receaved");
						}
						br.close();
						s.close();
					} catch (IOException ioe) {
						System.out.println( "IOExceptionin Client constructor: " + ioe.getMessage() );
					}
				}


			};
			new Thread(input).start();
			
			output = new Runnable(){
				public void run() {
					try {
						ObjectOutputStream br = new ObjectOutputStream(s.getOutputStream());
						int x = 2;
						while(x == 2)
						{
							
							System.out.println( "Waiting for message" );
							try {
								Message msg = (Message) br.readObject();
								messages.add(msg);
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println("Message Object Receaved");
						}
						br.close();
						s.close();
					} catch (IOException ioe) {
						System.out.println( "IOExceptionin Client constructor: " + ioe.getMessage() );
					}
				}


			};
			new Thread(output).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
