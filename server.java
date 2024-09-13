import java.io.BufferedReader;
// import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.net.*;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;


class server extends JFrame{
    ServerSocket Server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // declare components for GUI
    private JLabel heading = new JLabel("Server Area");
    private JTextArea msgarea = new JTextArea();
    private JTextField msginput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public server() //class constructor
    {
        try {
            Server = new ServerSocket(7778);
            System.out.println("Server is ready to accept connectiom from client");
            System.out.println("waiting...");
            socket = Server.accept();
            
            System.out.println("connection accepted");

            //reading the input from client using BufferedReader
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // printing the output using PrintWriter
            out = new PrintWriter(socket.getOutputStream(), true);

            createGUI();
            handleEvent();
            startReading();
            // startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    private void handleEvent() {
        msginput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // System.out.println("Key releases = "+e.getKeyChar());
                if(e.getKeyCode() == 10){
                    String conttosend = msginput.getText();
                    msgarea.append("Me: "+conttosend+"\n");
                    out.println(conttosend);
                    out.flush();
                    msginput.setText("");
                    msginput.requestFocus();
                }
            }
            
        });
    }

    private void createGUI() {
        //GUI code
        this.setTitle("Server Message[START]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        heading.setFont(font);
        msgarea.setFont(font);
        msginput.setFont(font);

        // heading.setIcon(new ImageIcon("clogo.png"));
        // heading.setHorizontalTextPosition(SwingConstants.CENTER);
        // heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        msgarea.setEditable(false);
        msginput.setHorizontalAlignment(SwingConstants.LEFT);

        //frame layout setting
        this.setLayout(new BorderLayout());

        //adding the components through frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(msgarea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(msginput, BorderLayout.SOUTH);
    }

    public void startReading(){
        Runnable r1 =()->{
            System.out.println("reading started...");
            try {
                while (true) {
                    String msg1=" ";
                    msg1= br.readLine();
                    if(msg1.equals("exit"))
                    {
                        System.out.println("Server disconnected");
                        JOptionPane.showMessageDialog(this, "Client Terminated");
                        msginput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Client says: "+msg);
                    msgarea.append("Client :"+ msg1+"\n");
                    // msgarea.setHorizontalAlignment(SwingConstants.LEFT);
                }
            } 
            catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection closed...");

            }
        };
        new Thread(r1).start();
    }
    public void startWriting(){
        Runnable r2=()->{
            System.out.println("Writing started...");
            try {
                while (!socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String cont = br1.readLine();
                    out.println(cont);
                    out.flush(); 
                    if(cont.equals("exit"))
                    {
                        socket.close();
                        break;
                    }                                   
                }
                System.out.println("Connection closed...");
            } 
            catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection closed...");

            }
            // System.out.println("Connection closed...");
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("This is server is going to start...");

        new server();

    }
}