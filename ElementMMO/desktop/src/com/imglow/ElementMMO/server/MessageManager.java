package com.imglow.ElementMMO.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import com.imglow.ElementMMO.BattleMessage;
import com.imglow.ElementMMO.EventMessage;
import com.imglow.ElementMMO.Message;
import com.imglow.ElementMMO.StatusMessage;
import com.imglow.ElementMMO.TextMessage;

public class MessageManager{
	private static MessageManager instance;
	private Vector<Message> queue;
	private Vector<TextMessage> textMessages;
	private Vector<StatusMessage> statusMessages;
	private Vector<EventMessage> eventMessages ;
	private Vector<BattleMessage> battleMessages;
	
	Socket s;
	Runnable input, output;
	Object msgLock = new Object();

	protected MessageManager(){
		queue = new Vector<Message>();
		textMessages = new Vector<TextMessage>();
		statusMessages = new Vector<StatusMessage>();
		eventMessages = new Vector<EventMessage>() ;
		battleMessages = new Vector<BattleMessage>();
	}

	public static MessageManager getInstance() {
		if(instance == null) {
			instance = new MessageManager();
		}
		return instance;
	}
	
	public void init(final Socket s)
	{
		this.s = s;
		
		input = new Runnable(){
			public void run() {
				try {
					ObjectInputStream br= new ObjectInputStream(s.getInputStream());
					int x = 2;
					while(x == 2)
					{
//						System.out.println( "Waiting for message" );
						try {
							Message msg = (Message) br.readObject();
							if(msg instanceof StatusMessage)
								statusMessages.add((StatusMessage) msg);
							else if(msg instanceof EventMessage)
								eventMessages.add((EventMessage) msg);
							else if(msg instanceof BattleMessage)
								battleMessages.add((BattleMessage) msg);
							else
								textMessages.add((TextMessage) msg);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						System.out.println("Message Object Receaved");
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
						if(queue.size() > 0)
						{
							br.writeObject(queue.get(0));
							queue.remove(0);
						}
						else
						{
							synchronized(msgLock)
							{
								try {
									msgLock.wait();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
					br.close();
					s.close();
				} catch (IOException ioe) {
					System.out.println( "IOExceptionin Client constructor: " + ioe.getMessage() );
				}
			}


		};
		new Thread(output).start();
	}
	
	public void sendMessageToServer(final Message msg)
	{
		new Thread(new Runnable(){
			@Override
			public void run() {
				queue.add(msg);
				
				synchronized(msgLock)
				{
					msgLock.notifyAll();
				}
			}}).start();
	}
	
	public boolean messageQueued()
	{
		return !queue.isEmpty();
	}
	
	public boolean hasStatusMessage()
	{
		return !statusMessages.isEmpty();
	}

	public StatusMessage getStatusMessage()
	{
		if(!hasStatusMessage())
			return null;
		
		StatusMessage temp = statusMessages.firstElement();
		statusMessages.remove(0);
		return temp;
	}
	
	public boolean hasEventMessage()
	{
		return !eventMessages.isEmpty();
	}

	public EventMessage getEventMessage()
	{
		if(!hasEventMessage())
			return null;
		
		EventMessage temp = eventMessages.firstElement();
		eventMessages.remove(0);
		return temp;
	}
	
	public boolean hasTextMessage()
	{
		return !textMessages.isEmpty();
	}

	public TextMessage getTextMessage()
	{
		if(!hasTextMessage())
			return null;
		
		TextMessage temp = textMessages.firstElement();
		textMessages.remove(0);
		return temp;
	}
	
	public boolean hasBattleMessage()
	{
		return !battleMessages.isEmpty();
	}

	public Message getBattleMessage()
	{
		if(!hasBattleMessage())
			return null;
		
		BattleMessage temp = battleMessages.firstElement();
		battleMessages.remove(0);
		return temp;
	}
	
}
