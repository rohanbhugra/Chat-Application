import java.net.*;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;


import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.*;
class Server extends JFrame{

    ServerSocket server;
    Socket socket;
    

    BufferedReader br; // for reading
    PrintWriter out; // for writing


    // Declare components
     private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea =  new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    //Constructor
    public Server()
    {
        try{
        server=new ServerSocket(7777);
        System.out.println("Server is ready to accept connection");
        System.out.println("Waiting...");
        socket=server.accept();
        
        br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());

        createGUI();
        handleEvents();
        startReading();
        //startWriting();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

     private void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
               // System.out.println("key released "+e.getKeyCode());
                if(e.getKeyCode()==10){
                    //System.out.println("You have pressed enter button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }
                
            }

        });
    }

    private void createGUI(){
        this.setTitle("Server Messenger[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        heading.setIcon(new ImageIcon("clogo.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        // frame Layout settings
        this.setLayout(new BorderLayout());
        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }
    
    public void startReading()
    {
        // thread-  it will read data 

        Runnable r1=()->{
            System.out.println("Reader started..");
            try{
            while(true)
            {
                
                String msg= br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Client terminated the chat");
                    socket.close();
                    break;
                }
                //System.out.println("Client : "+msg);
                  
                messageArea.append("Client : "+msg+"\n");
            }
        }
            catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection closed");
            }
            };
        
        

        new Thread(r1).start();
    }

    public void startWriting()
    {
        // thread- it will take data from user and then send it to client
        Runnable r2=()->{
            System.out.println("Writer started..");
            try{
            while( !socket.isClosed() )
            {
                
                
                    BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                   
                    out.println(content);
                    out.flush();
                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
            }   
            
        } 
                catch(Exception e)
                {
                    //TODO: handle exception
                    //e.printStackTrace();
                    System.out.println("Connection closed");
                }

            };
    

        new Thread(r2).start();
    }
    public static void main(String[] args) {
     System.out.println("This is server..going to start server");
     
     new Server();
    }
}