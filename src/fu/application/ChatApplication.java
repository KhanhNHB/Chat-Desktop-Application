package fu.application;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import fu.client.ClientDAO;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import fu.client.ClientDTO;
import fu.server.Server;
import fu.webcam.ImageCapture;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatApplication extends javax.swing.JFrame {

    private final String hostServer = "localhost";
    private final int portServer = 9999;
    private Socket socket;
    private DefaultListModel<ClientDTO> listModel = new DefaultListModel();
    private ClientDTO dto;
    private SimpleAttributeSet left;
    private SimpleAttributeSet right;
    private StyledDocument docMessage;
    private File file;
    private BufferedImage bi;
    private ImageIcon imageIcon;
    private String fileContents;
    private boolean isOpen = false;
    private boolean isOpenEmojiPanel = false;
    private ArrayList<Emoji> emoji;

    public ChatApplication() {
    }

    public ChatApplication(ClientDTO dto) throws IOException {
        initComponents();
        this.dto = dto;
        loadApplication();
        loadHomePanel();
        loadListEmoji();
        setVisible(true);
    }

    public void logoutSession() {
        if (this.socket == null) {
            return;
        }
        try {
            DataOutputStream dos = new DataOutputStream(this.socket.getOutputStream());
            dos.writeUTF(Server.LOGOUT_MESSAGE);
            this.socket = null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadApplication() {
        pnlMenu.setSize(0, 450);
        lblMenu.setSize(40, 40);
        lblLeftArrow.setSize(0, 0);

        pnlEmojiMain.setSize(0, 0);
        pnlEmojiMain.revalidate();

        txtMessages.setWrapStyleWord(true);
        txtMessages.setLineWrap(true);
        txtMessages.setRows(1);

        lblFullnameWelcome.setText(this.dto.getFullname());
        lblLogoAppHeader.setText(this.dto.getFullname());
        byte[] avatar = this.dto.getAvarta();
        if (avatar != null) {
            imageIcon = new ImageIcon(new ImageIcon(avatar).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            lblLogoAppBody.setIcon(imageIcon);
        }
        try {
            connectServer();
        } catch (IOException ex) {
        }
        docMessage = txtMessage.getStyledDocument();
        left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(left, Color.BLACK);
        StyleConstants.setBackground(left, Color.LIGHT_GRAY);
        StyleConstants.setFontSize(left, 18);
        right = new SimpleAttributeSet();
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(right, Color.WHITE);
        StyleConstants.setBackground(right, Color.black);
        StyleConstants.setFontSize(right, 18);

    }

    private void connectServer() throws IOException {
        socket = new Socket(hostServer, portServer);
        if (socket != null) {
            try {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(this.dto.getUsername());
                Thread readMessage = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            String msg;
                            try {
                                msg = dis.readUTF();
                                if (msg.startsWith(Server.UPDATE_USERS)) {
                                    updateUsersList(msg);
                                } else if (msg.equals(Server.LOGOUT_MESSAGE)) {
                                    break;
                                } else if (msg.contains("FileName://")) {
                                    StringTokenizer stk = new StringTokenizer(msg, "|");
                                    stk.nextToken();
                                    String fileName = stk.nextToken();
                                    String contentFile = stk.nextToken();
                                    messegesLeftTablePane(lblFullnameWelcome.getText() + ": " + fileName);
                                    int chooice = JOptionPane.showConfirmDialog(null, "Do you want to save file: "
                                            + fileName, lblFullnameWelcome.getText()
                                            + " has sent you a file", JOptionPane.YES_NO_OPTION);
                                    if (chooice == JOptionPane.YES_OPTION) {
                                        saveFile(fileName, contentFile);
                                    }
                                } else if (msg.contains("Picture://")) {
                                    StringTokenizer stk = new StringTokenizer(msg, "|");
                                    stk.nextToken();
                                    String filePath = stk.nextToken();
                                    bi = ImageIO.read(new File(filePath));
                                    messegesLeftTablePane("Anonymous.jpg");
                                    int chooice = JOptionPane.showConfirmDialog(null, "Do you want to save file Anonymous.jpg? ", lblFullnameWelcome.getText() + " has sent you a file", JOptionPane.YES_NO_OPTION);
                                    if (chooice == JOptionPane.YES_OPTION) {
                                        ImageIO.write(bi, "jpg", new File("Anonymous.png"));
                                    }
                                } else {
                                    messegesLeftTablePane(msg.trim());
                                }
                            } catch (IOException | BadLocationException | SQLException e) {
                            }
                        }
                    }
                });
                readMessage.start();
            } catch (IOException e) {
            }
        }
    }

    private void messegesLeftTablePane(String msg) throws BadLocationException {
        docMessage.insertString(docMessage.getLength(), "\n" + listContact.getSelectedValue().getFullname() + ": " + msg.trim() + "\t", left);
        docMessage.setParagraphAttributes(docMessage.getLength(), 1, left, false);
    }

    private void messegesRightTablePane(String msg) throws BadLocationException {
        docMessage.insertString(docMessage.getLength(), "\n" + this.dto.getFullname() + ": " + msg.trim() + "\t", right);
        docMessage.setParagraphAttributes(docMessage.getLength(), 1, right, false);
    }

    private void loadListEmoji() {
        emoji = (ArrayList<Emoji>) EmojiManager.getAll();
        lbl1.setText(emoji.get(0).getUnicode());
        lbl2.setText(emoji.get(1).getUnicode());
        lbl3.setText(emoji.get(2).getUnicode());
        lbl4.setText(emoji.get(3).getUnicode());
        lbl5.setText(emoji.get(4).getUnicode());
        lbl6.setText(emoji.get(5).getUnicode());
        lbl7.setText(emoji.get(6).getUnicode());
        lbl8.setText(emoji.get(7).getUnicode());
        lbl9.setText(emoji.get(8).getUnicode());
        lbl10.setText(emoji.get(9).getUnicode());
        lbl11.setText(emoji.get(10).getUnicode());
        lbl12.setText(emoji.get(11).getUnicode());
        lbl13.setText(emoji.get(12).getUnicode());
        lbl14.setText(emoji.get(13).getUnicode());
        lbl15.setText(emoji.get(14).getUnicode());
    }

    private void saveFile(String fileName, String content) {
        try {
            PrintWriter pw = new PrintWriter(fileName);
            StringTokenizer stk = new StringTokenizer(content, "\n");
            while (stk.hasMoreTokens()) {
                pw.println(stk.nextToken());
            }
            pw.close();
        } catch (FileNotFoundException e) {
        }
    }

    public void updateUsersList(String msg) throws SQLException {
        Vector<ClientDTO> listUser = new Vector();
        msg = msg.replace("[", "");
        msg = msg.replace("]", "");
        msg = msg.replace(Server.UPDATE_USERS, "");
        StringTokenizer stk = new StringTokenizer(msg, ",");
        while (stk.hasMoreTokens()) {
            String user = stk.nextToken().trim();
            ClientDTO dto = new ClientDAO().getClientByUsername(user);
            listUser.add(dto);
        }

        listModel.removeAllElements();
        for (ClientDTO dto : listUser) {
            if (!dto.getUsername().equals(this.dto.getUsername())) {
                listModel.addElement(dto);
            }
        }
        listContact.setBackground(Color.white);
        listContact.removeAll();
        listContact.setModel(listModel);
        listContact.setCellRenderer(new ClientRenderer());
    }

    public class ClientRenderer extends JLabel implements ListCellRenderer<ClientDTO> {

        public ClientRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ClientDTO> listDTO, ClientDTO dto, int i, boolean isSelected, boolean bln1) {
            setSize(new Dimension(40, 210));
            byte[] avatar = dto.getAvarta();
            imageIcon = new ImageIcon(new ImageIcon(avatar).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            setIcon(imageIcon);
            setText(dto.getFullname());
            setBorder(new EmptyBorder(10, 0, 10, 10));
            if (isSelected) {
                setBackground(listDTO.getSelectionBackground());
                setForeground(listDTO.getSelectionForeground());
            } else {
                setBackground(listDTO.getBackground());
                setForeground(listDTO.getForeground());
            }
            return this;
        }
    }

    private void loadHomePanel() {
        pnlBody.removeAll();
        pnlBody.add(pnlHomeDemo);
        pnlBody.repaint();
        pnlBody.revalidate();
        pnlHome.setBackground(new Color(110, 89, 222));
        pnlCollection.setBackground(new Color(85, 65, 118));
        pnlChat.setBackground(new Color(85, 65, 118));
    }

    private void loadChatPanel() {
        pnlBody.removeAll();
        pnlBody.add(pnlChatDemo);
        pnlBody.repaint();
        pnlBody.revalidate();
        pnlHome.setBackground(new Color(85, 65, 118));
        pnlCollection.setBackground(new Color(85, 65, 118));
        pnlChat.setBackground(new Color(110, 89, 222));
    }

    private void loadCollectionPanel() {
        pnlBody.removeAll();
        pnlBody.add(pnlCollectionsDemo);
        pnlBody.repaint();
        pnlBody.revalidate();
        pnlHome.setBackground(new Color(85, 65, 118));
        pnlCollection.setBackground(new Color(110, 89, 222));
        pnlChat.setBackground(new Color(85, 65, 118));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblLeftArrow = new javax.swing.JLabel();
        lblMenu = new javax.swing.JLabel();
        lblExit = new javax.swing.JLabel();
        pnlMenu = new javax.swing.JPanel();
        pnlLogo = new javax.swing.JPanel();
        lblLogoBody = new javax.swing.JLabel();
        pnlHome = new javax.swing.JPanel();
        lblHome = new javax.swing.JLabel();
        lblTextHome = new javax.swing.JLabel();
        pnlCollection = new javax.swing.JPanel();
        lblCollections1 = new javax.swing.JLabel();
        lblTextCollections1 = new javax.swing.JLabel();
        pnlChat = new javax.swing.JPanel();
        lblCollections = new javax.swing.JLabel();
        lblTextChat = new javax.swing.JLabel();
        pnlLogoApp = new javax.swing.JPanel();
        lblLogoAppHeader = new javax.swing.JLabel();
        pnlLogoAppBody = new javax.swing.JPanel();
        lblLogoAppBody = new javax.swing.JLabel();
        pnlEmojiMain = new javax.swing.JPanel();
        pnlEmojiHeader = new javax.swing.JPanel();
        btnEmojiExit = new javax.swing.JButton();
        lblEmoji = new javax.swing.JLabel();
        pnlEmojiBody = new javax.swing.JPanel();
        lbl1 = new javax.swing.JLabel();
        lbl2 = new javax.swing.JLabel();
        lbl3 = new javax.swing.JLabel();
        lbl4 = new javax.swing.JLabel();
        lbl5 = new javax.swing.JLabel();
        lbl6 = new javax.swing.JLabel();
        lbl7 = new javax.swing.JLabel();
        lbl8 = new javax.swing.JLabel();
        lbl9 = new javax.swing.JLabel();
        lbl10 = new javax.swing.JLabel();
        lbl11 = new javax.swing.JLabel();
        lbl12 = new javax.swing.JLabel();
        lbl13 = new javax.swing.JLabel();
        lbl14 = new javax.swing.JLabel();
        lbl15 = new javax.swing.JLabel();
        pnlBody = new javax.swing.JPanel();
        pnlHomeDemo = new javax.swing.JPanel();
        lblHomeHeader = new javax.swing.JLabel();
        lblWelcome = new javax.swing.JLabel();
        lblFullnameWelcome = new javax.swing.JLabel();
        pnlChatDemo = new javax.swing.JPanel();
        pnlChatContent = new javax.swing.JPanel();
        txtMessage = new javax.swing.JTextPane();
        pnlContact = new javax.swing.JPanel();
        scrollListContact = new javax.swing.JScrollPane();
        listContact = new javax.swing.JList<>();
        lblContact = new javax.swing.JLabel();
        pnlComposeMessage = new javax.swing.JPanel();
        scrollMesseges = new javax.swing.JScrollPane();
        txtMessages = new javax.swing.JTextArea();
        pnlSendEvent = new javax.swing.JPanel();
        btnSendFile = new javax.swing.JButton();
        btnEmoji = new javax.swing.JButton();
        btnCamera = new javax.swing.JButton();
        btnSendMessage = new javax.swing.JButton();
        lblFullName = new javax.swing.JLabel();
        pnlCollectionsDemo = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat Application");
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(800, 445));
        setUndecorated(true);
        setSize(new java.awt.Dimension(1000, 445));
        getContentPane().setLayout(null);

        pnlHeader.setBackground(new java.awt.Color(153, 0, 153));
        pnlHeader.setLayout(null);

        lblLeftArrow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLeftArrow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/left-arrow.png"))); // NOI18N
        lblLeftArrow.setToolTipText("");
        lblLeftArrow.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblLeftArrow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblLeftArrowMousePressed(evt);
            }
        });
        pnlHeader.add(lblLeftArrow);
        lblLeftArrow.setBounds(0, 0, 40, 40);

        lblMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/menu.png"))); // NOI18N
        lblMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblMenuMousePressed(evt);
            }
        });
        pnlHeader.add(lblMenu);
        lblMenu.setBounds(0, 0, 40, 40);

        lblExit.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        lblExit.setForeground(new java.awt.Color(255, 255, 255));
        lblExit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/delete-cross.png"))); // NOI18N
        lblExit.setToolTipText("");
        lblExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblExitMouseClicked(evt);
            }
        });
        pnlHeader.add(lblExit);
        lblExit.setBounds(960, 0, 40, 40);

        getContentPane().add(pnlHeader);
        pnlHeader.setBounds(0, 0, 1000, 40);

        pnlMenu.setBackground(new java.awt.Color(102, 0, 102));
        pnlMenu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlMenu.setMaximumSize(new java.awt.Dimension(250, 450));
        pnlMenu.setMinimumSize(new java.awt.Dimension(250, 450));
        pnlMenu.setPreferredSize(new java.awt.Dimension(250, 450));
        pnlMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlLogo.setBackground(new java.awt.Color(85, 65, 118));
        pnlLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlLogo.setMaximumSize(new java.awt.Dimension(250, 60));
        pnlLogo.setMinimumSize(new java.awt.Dimension(250, 60));
        pnlLogo.setPreferredSize(new java.awt.Dimension(250, 60));
        pnlLogo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblLogoBody.setBackground(new java.awt.Color(102, 0, 204));
        lblLogoBody.setForeground(new java.awt.Color(255, 255, 255));
        lblLogoBody.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogoBody.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/frog.png"))); // NOI18N
        pnlLogo.add(lblLogoBody, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 160));

        pnlMenu.add(pnlLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 160));

        pnlHome.setBackground(new java.awt.Color(85, 65, 118));
        pnlHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlHome.setMaximumSize(new java.awt.Dimension(250, 60));
        pnlHome.setMinimumSize(new java.awt.Dimension(250, 60));
        pnlHome.setPreferredSize(new java.awt.Dimension(250, 60));
        pnlHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlHomeMousePressed(evt);
            }
        });
        pnlHome.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblHome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/home.png"))); // NOI18N
        pnlHome.add(lblHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 40, 40));

        lblTextHome.setBackground(new java.awt.Color(85, 65, 118));
        lblTextHome.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        lblTextHome.setForeground(new java.awt.Color(255, 255, 255));
        lblTextHome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTextHome.setText("Home");
        pnlHome.add(lblTextHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 40));

        pnlMenu.add(pnlHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 250, 40));

        pnlCollection.setBackground(new java.awt.Color(85, 65, 118));
        pnlCollection.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlCollection.setMaximumSize(new java.awt.Dimension(250, 60));
        pnlCollection.setMinimumSize(new java.awt.Dimension(250, 60));
        pnlCollection.setPreferredSize(new java.awt.Dimension(250, 60));
        pnlCollection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlCollectionMousePressed(evt);
            }
        });
        pnlCollection.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCollections1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCollections1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/fonts-collection.png"))); // NOI18N
        pnlCollection.add(lblCollections1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 40, 40));

        lblTextCollections1.setBackground(new java.awt.Color(85, 65, 118));
        lblTextCollections1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        lblTextCollections1.setForeground(new java.awt.Color(255, 255, 255));
        lblTextCollections1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTextCollections1.setText("Collections");
        pnlCollection.add(lblTextCollections1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 40));

        pnlMenu.add(pnlCollection, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 250, 40));

        pnlChat.setBackground(new java.awt.Color(85, 65, 118));
        pnlChat.setMaximumSize(new java.awt.Dimension(250, 60));
        pnlChat.setMinimumSize(new java.awt.Dimension(250, 60));
        pnlChat.setPreferredSize(new java.awt.Dimension(250, 60));
        pnlChat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlChatMousePressed(evt);
            }
        });
        pnlChat.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCollections.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCollections.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/chat.png"))); // NOI18N
        pnlChat.add(lblCollections, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 40, 40));

        lblTextChat.setBackground(new java.awt.Color(85, 65, 118));
        lblTextChat.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        lblTextChat.setForeground(new java.awt.Color(255, 255, 255));
        lblTextChat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTextChat.setText("Chat");
        pnlChat.add(lblTextChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 40));

        pnlMenu.add(pnlChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 250, 40));

        getContentPane().add(pnlMenu);
        pnlMenu.setBounds(0, 40, 250, 410);

        pnlLogoApp.setBackground(new java.awt.Color(102, 0, 102));
        pnlLogoApp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 0));
        pnlLogoApp.setAutoscrolls(true);
        pnlLogoApp.setMaximumSize(new java.awt.Dimension(210, 410));
        pnlLogoApp.setMinimumSize(new java.awt.Dimension(210, 410));
        pnlLogoApp.setName(""); // NOI18N
        pnlLogoApp.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblLogoAppHeader.setBackground(new java.awt.Color(255, 255, 255));
        lblLogoAppHeader.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        lblLogoAppHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblLogoAppHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogoAppHeader.setText("Contacts");
        lblLogoAppHeader.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 0, 102), 1, true));
        lblLogoAppHeader.setMaximumSize(new java.awt.Dimension(160, 38));
        lblLogoAppHeader.setMinimumSize(new java.awt.Dimension(160, 38));
        lblLogoAppHeader.setPreferredSize(new java.awt.Dimension(160, 38));
        pnlLogoApp.add(lblLogoAppHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, -1));

        pnlLogoAppBody.setBackground(new java.awt.Color(255, 255, 255));
        pnlLogoAppBody.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 102), 0));
        pnlLogoAppBody.setPreferredSize(new java.awt.Dimension(250, 380));

        lblLogoAppBody.setBackground(new java.awt.Color(255, 255, 255));
        lblLogoAppBody.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogoAppBody.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/frog.png"))); // NOI18N
        lblLogoAppBody.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout pnlLogoAppBodyLayout = new javax.swing.GroupLayout(pnlLogoAppBody);
        pnlLogoAppBody.setLayout(pnlLogoAppBodyLayout);
        pnlLogoAppBodyLayout.setHorizontalGroup(
            pnlLogoAppBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnlLogoAppBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlLogoAppBodyLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(lblLogoAppBody, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        pnlLogoAppBodyLayout.setVerticalGroup(
            pnlLogoAppBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
            .addGroup(pnlLogoAppBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlLogoAppBodyLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(lblLogoAppBody, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pnlLogoApp.add(pnlLogoAppBody, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 230, 360));

        getContentPane().add(pnlLogoApp);
        pnlLogoApp.setBounds(0, 40, 250, 410);

        pnlEmojiMain.setMaximumSize(new java.awt.Dimension(250, 160));
        pnlEmojiMain.setPreferredSize(new java.awt.Dimension(250, 160));

        pnlEmojiHeader.setBackground(new java.awt.Color(102, 0, 102));
        pnlEmojiHeader.setMaximumSize(new java.awt.Dimension(250, 23));
        pnlEmojiHeader.setMinimumSize(new java.awt.Dimension(250, 23));
        pnlEmojiHeader.setPreferredSize(new java.awt.Dimension(250, 23));

        btnEmojiExit.setBackground(new java.awt.Color(85, 65, 118));
        btnEmojiExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/delete-cross.png"))); // NOI18N
        btnEmojiExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEmojiExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmojiExitActionPerformed(evt);
            }
        });

        lblEmoji.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        lblEmoji.setForeground(new java.awt.Color(255, 255, 255));
        lblEmoji.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEmoji.setText("   Emojis");

        javax.swing.GroupLayout pnlEmojiHeaderLayout = new javax.swing.GroupLayout(pnlEmojiHeader);
        pnlEmojiHeader.setLayout(pnlEmojiHeaderLayout);
        pnlEmojiHeaderLayout.setHorizontalGroup(
            pnlEmojiHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEmojiHeaderLayout.createSequentialGroup()
                .addComponent(lblEmoji, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(btnEmojiExit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlEmojiHeaderLayout.setVerticalGroup(
            pnlEmojiHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmojiHeaderLayout.createSequentialGroup()
                .addGroup(pnlEmojiHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblEmoji, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEmojiExit, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlEmojiBody.setBackground(new java.awt.Color(255, 255, 255));
        pnlEmojiBody.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 0, 204)));
        pnlEmojiBody.setMaximumSize(new java.awt.Dimension(250, 410));
        pnlEmojiBody.setMinimumSize(new java.awt.Dimension(250, 410));
        pnlEmojiBody.setPreferredSize(new java.awt.Dimension(250, 410));
        pnlEmojiBody.setLayout(new java.awt.GridLayout(6, 6));

        lbl1.setBackground(new java.awt.Color(255, 255, 255));
        lbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl1.setText("jLabel1");
        lbl1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl1MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl1);

        lbl2.setBackground(new java.awt.Color(255, 255, 255));
        lbl2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl2.setText("jLabel1");
        lbl2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl2MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl2);

        lbl3.setBackground(new java.awt.Color(255, 255, 255));
        lbl3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl3.setText("jLabel1");
        lbl3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl3MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl3);

        lbl4.setBackground(new java.awt.Color(255, 255, 255));
        lbl4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl4.setText("jLabel1");
        lbl4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl4MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl4);

        lbl5.setBackground(new java.awt.Color(255, 255, 255));
        lbl5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl5.setText("jLabel1");
        lbl5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl5MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl5);

        lbl6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl6.setText("jLabel1");
        lbl6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl6MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl6);

        lbl7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl7.setText("jLabel1");
        lbl7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl7MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl7);

        lbl8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl8.setText("jLabel1");
        lbl8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl8MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl8);

        lbl9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl9.setText("jLabel1");
        lbl9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl9MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl9);

        lbl10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl10.setText("jLabel1");
        lbl10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl10MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl10);

        lbl11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl11.setText("jLabel1");
        lbl11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl11MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl11);

        lbl12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl12.setText("jLabel1");
        lbl12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl12MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl12);

        lbl13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl13.setText("jLabel1");
        lbl13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl13MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl13);

        lbl14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl14.setText("jLabel1");
        lbl14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl14MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl14);

        lbl15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl15.setText("jLabel1");
        lbl15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl15MousePressed(evt);
            }
        });
        pnlEmojiBody.add(lbl15);

        javax.swing.GroupLayout pnlEmojiMainLayout = new javax.swing.GroupLayout(pnlEmojiMain);
        pnlEmojiMain.setLayout(pnlEmojiMainLayout);
        pnlEmojiMainLayout.setHorizontalGroup(
            pnlEmojiMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlEmojiHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlEmojiBody, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnlEmojiMainLayout.setVerticalGroup(
            pnlEmojiMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmojiMainLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(pnlEmojiHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlEmojiBody, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(pnlEmojiMain);
        pnlEmojiMain.setBounds(0, 40, 250, 410);

        pnlBody.setBackground(new java.awt.Color(255, 255, 255));
        pnlBody.setLayout(new java.awt.CardLayout());

        pnlHomeDemo.setBackground(new java.awt.Color(255, 255, 255));

        lblHomeHeader.setBackground(new java.awt.Color(85, 65, 118));
        lblHomeHeader.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        lblHomeHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblHomeHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHomeHeader.setText("Home");

        lblWelcome.setBackground(new java.awt.Color(255, 255, 255));
        lblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWelcome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/welcome.jpg"))); // NOI18N

        lblFullnameWelcome.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        lblFullnameWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlHomeDemoLayout = new javax.swing.GroupLayout(pnlHomeDemo);
        pnlHomeDemo.setLayout(pnlHomeDemoLayout);
        pnlHomeDemoLayout.setHorizontalGroup(
            pnlHomeDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
            .addComponent(lblFullnameWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlHomeDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblHomeHeader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE))
        );
        pnlHomeDemoLayout.setVerticalGroup(
            pnlHomeDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHomeDemoLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFullnameWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
            .addGroup(pnlHomeDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlHomeDemoLayout.createSequentialGroup()
                    .addComponent(lblHomeHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 370, Short.MAX_VALUE)))
        );

        pnlBody.add(pnlHomeDemo, "card2");

        pnlChatDemo.setBackground(new java.awt.Color(102, 0, 102));
        pnlChatDemo.setLayout(null);

        pnlChatContent.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtMessage.setEditable(false);
        txtMessage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        txtMessage.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N

        javax.swing.GroupLayout pnlChatContentLayout = new javax.swing.GroupLayout(pnlChatContent);
        pnlChatContent.setLayout(pnlChatContentLayout);
        pnlChatContentLayout.setHorizontalGroup(
            pnlChatContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtMessage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
        );
        pnlChatContentLayout.setVerticalGroup(
            pnlChatContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
        );

        pnlChatDemo.add(pnlChatContent);
        pnlChatContent.setBounds(0, 40, 480, 310);

        pnlContact.setBackground(new java.awt.Color(102, 0, 102));
        pnlContact.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 0));
        pnlContact.setAutoscrolls(true);
        pnlContact.setMaximumSize(new java.awt.Dimension(210, 410));
        pnlContact.setMinimumSize(new java.awt.Dimension(210, 410));
        pnlContact.setName(""); // NOI18N

        scrollListContact.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        scrollListContact.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollListContact.setAutoscrolls(true);

        listContact.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 102)));
        listContact.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        listContact.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listContactMouseClicked(evt);
            }
        });
        scrollListContact.setViewportView(listContact);

        lblContact.setBackground(new java.awt.Color(255, 255, 255));
        lblContact.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        lblContact.setForeground(new java.awt.Color(255, 255, 255));
        lblContact.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblContact.setText("Contacts");
        lblContact.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 0, 102), 1, true));
        lblContact.setMaximumSize(new java.awt.Dimension(160, 38));
        lblContact.setMinimumSize(new java.awt.Dimension(160, 38));
        lblContact.setPreferredSize(new java.awt.Dimension(160, 38));

        javax.swing.GroupLayout pnlContactLayout = new javax.swing.GroupLayout(pnlContact);
        pnlContact.setLayout(pnlContactLayout);
        pnlContactLayout.setHorizontalGroup(
            pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollListContact, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(lblContact, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlContactLayout.setVerticalGroup(
            pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContactLayout.createSequentialGroup()
                .addComponent(lblContact, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(scrollListContact, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pnlChatDemo.add(pnlContact);
        pnlContact.setBounds(490, 0, 250, 400);

        pnlComposeMessage.setBackground(new java.awt.Color(255, 255, 255));
        pnlComposeMessage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlComposeMessage.setMaximumSize(new java.awt.Dimension(580, 50));
        pnlComposeMessage.setRequestFocusEnabled(false);

        scrollMesseges.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        scrollMesseges.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollMesseges.setAutoscrolls(true);

        txtMessages.setColumns(20);
        txtMessages.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        txtMessages.setRows(5);
        txtMessages.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        scrollMesseges.setViewportView(txtMessages);

        pnlSendEvent.setBackground(new java.awt.Color(255, 255, 255));
        pnlSendEvent.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        pnlSendEvent.setLayout(new java.awt.GridLayout(1, 3, 20, 20));

        btnSendFile.setBackground(new java.awt.Color(85, 65, 118));
        btnSendFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/folder.png"))); // NOI18N
        btnSendFile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSendFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendFileActionPerformed(evt);
            }
        });
        pnlSendEvent.add(btnSendFile);

        btnEmoji.setBackground(new java.awt.Color(85, 65, 118));
        btnEmoji.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/smile.png"))); // NOI18N
        btnEmoji.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEmoji.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEmojiMousePressed(evt);
            }
        });
        pnlSendEvent.add(btnEmoji);

        btnCamera.setBackground(new java.awt.Color(85, 65, 118));
        btnCamera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/photo-camera.png"))); // NOI18N
        btnCamera.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCamera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCameraActionPerformed(evt);
            }
        });
        pnlSendEvent.add(btnCamera);

        btnSendMessage.setBackground(new java.awt.Color(85, 65, 118));
        btnSendMessage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/images/send.png"))); // NOI18N
        btnSendMessage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSendMessage.setMaximumSize(new java.awt.Dimension(80, 80));
        btnSendMessage.setMinimumSize(new java.awt.Dimension(80, 80));
        btnSendMessage.setPreferredSize(new java.awt.Dimension(80, 80));
        btnSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMessageActionPerformed(evt);
            }
        });
        pnlSendEvent.add(btnSendMessage);

        javax.swing.GroupLayout pnlComposeMessageLayout = new javax.swing.GroupLayout(pnlComposeMessage);
        pnlComposeMessage.setLayout(pnlComposeMessageLayout);
        pnlComposeMessageLayout.setHorizontalGroup(
            pnlComposeMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlComposeMessageLayout.createSequentialGroup()
                .addComponent(scrollMesseges, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlSendEvent, javax.swing.GroupLayout.PREFERRED_SIZE, 240, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        pnlComposeMessageLayout.setVerticalGroup(
            pnlComposeMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollMesseges, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
            .addComponent(pnlSendEvent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        pnlChatDemo.add(pnlComposeMessage);
        pnlComposeMessage.setBounds(0, 360, 480, 40);

        lblFullName.setBackground(new java.awt.Color(255, 255, 255));
        lblFullName.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        lblFullName.setForeground(new java.awt.Color(255, 255, 255));
        lblFullName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFullName.setText("Messeger");
        lblFullName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 0, 102), 1, true));
        lblFullName.setMaximumSize(new java.awt.Dimension(580, 38));
        lblFullName.setMinimumSize(new java.awt.Dimension(580, 38));
        lblFullName.setPreferredSize(new java.awt.Dimension(580, 38));
        pnlChatDemo.add(lblFullName);
        lblFullName.setBounds(0, 0, 480, 38);

        pnlBody.add(pnlChatDemo, "card3");

        javax.swing.GroupLayout pnlCollectionsDemoLayout = new javax.swing.GroupLayout(pnlCollectionsDemo);
        pnlCollectionsDemo.setLayout(pnlCollectionsDemoLayout);
        pnlCollectionsDemoLayout.setHorizontalGroup(
            pnlCollectionsDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
        pnlCollectionsDemoLayout.setVerticalGroup(
            pnlCollectionsDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 410, Short.MAX_VALUE)
        );

        pnlBody.add(pnlCollectionsDemo, "card2");

        getContentPane().add(pnlBody);
        pnlBody.setBounds(250, 40, 750, 410);

        setSize(new java.awt.Dimension(1000, 449));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMenuMousePressed
        pnlEmojiMain.setSize(0, 0);
        pnlEmojiMain.revalidate();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i <= 6; i++) {
                        if (i == 1) {
                            pnlMenu.setSize(0, 440);
                            lblMenu.setSize(0, 0);
                            lblLeftArrow.setSize(40, 40);
                        }
                        if (i == 2) {
                            pnlMenu.setSize(50, 440);
                        }
                        if (i == 3) {
                            pnlMenu.setSize(100, 440);
                        }
                        if (i == 4) {
                            pnlMenu.setSize(150, 440);
                        }
                        if (i == 5) {
                            pnlMenu.setSize(200, 440);
                        }
                        if (i == 6) {
                            pnlMenu.setSize(250, 440);
                        }
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                }
            }
        });
        if (!isOpen) {
            thread.start();
            isOpen = true;
        }
    }//GEN-LAST:event_lblMenuMousePressed

    private void lblLeftArrowMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLeftArrowMousePressed
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i <= 6; i++) {
                        if (i == 1) {
                            pnlMenu.setSize(250, 440);
                            lblLeftArrow.setSize(0, 0);
                        }
                        if (i == 2) {
                            pnlMenu.setSize(200, 440);
                        }
                        if (i == 3) {
                            pnlMenu.setSize(150, 440);
                        }
                        if (i == 4) {
                            pnlMenu.setSize(100, 440);
                        }
                        if (i == 5) {
                            pnlMenu.setSize(50, 440);
                        }
                        if (i == 6) {
                            lblMenu.setSize(40, 40);
                            pnlMenu.setSize(0, 440);
                        }
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                }
            }
        });
        if (isOpen) {
            thread.start();
            isOpen = false;
        }
    }//GEN-LAST:event_lblLeftArrowMousePressed

    private void pnlHomeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlHomeMousePressed
        loadHomePanel();
    }//GEN-LAST:event_pnlHomeMousePressed

    private void pnlCollectionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlCollectionMousePressed
        loadCollectionPanel();
    }//GEN-LAST:event_pnlCollectionMousePressed

    private void pnlChatMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlChatMousePressed
        loadChatPanel();
    }//GEN-LAST:event_pnlChatMousePressed

    private void lblExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblExitMouseClicked
        logoutSession();
        System.exit(0);
    }//GEN-LAST:event_lblExitMouseClicked

    private void listContactMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listContactMouseClicked
        String fullname = listContact.getSelectedValue().getFullname();
        if (fullname != null) {
            lblFullName.setText(fullname);
            txtMessage.removeAll();
        }
    }//GEN-LAST:event_listContactMouseClicked

    private void btnSendFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendFileActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        StringBuilder sb = new StringBuilder();

        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String details;
                while ((details = br.readLine()) != null) {
                    sb.append(details);
                    sb.append("\n");
                }
                String fileName = file.getName();

                if (fileName.contains(".jpg") || fileName.contains(".png") || fileName.contains(".jpeg")) {
                    fileContents = "\nPicture://|" + file.getAbsolutePath();
                    txtMessages.setText(fileName);
                } else {
                    fileContents = "\nFileName://|" + fileName + "|" + sb;
                    txtMessages.setText(fileName);
                }
                br.close();
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
        } else {
            System.out.println("Not found file");
        }
    }//GEN-LAST:event_btnSendFileActionPerformed

    private void btnCameraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCameraActionPerformed
        setVisible(true);
        new ImageCapture();
    }//GEN-LAST:event_btnCameraActionPerformed

    private void btnSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMessageActionPerformed
        try {
            String send = txtMessages.getText();
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            if (lblFullName.getText().equals("Messsger")) {
                JOptionPane.showMessageDialog(this, "Please choice user to send messages.");
                return;
            }

            if (fileContents != null && file.getName().contains(".txt") && fileContents.contains("FileName://|")) {
                dos.writeUTF(fileContents + "#" + lblFullName.getText().trim());
                dos.flush();

                docMessage.insertString(docMessage.getLength(), "\n    " + file.getName() + "\t", right);
                docMessage.setParagraphAttributes(docMessage.getLength(), 1, right, false);

                txtMessages.setText("");
                fileContents = null;
            } else if (fileContents != null
                    && (file.getName().contains(".jpg")
                    || file.getName().contains(".png")
                    || file.getName().contains(".jpeg"))
                    && fileContents.contains("Picture://|")) {

                dos.writeUTF(fileContents + "#" + lblFullName.getText().trim());
                dos.flush();

                messegesRightTablePane("Anonymouse.jpg");

                txtMessages.setText("");
                fileContents = null;
            } else {
                String username = listContact.getSelectedValue().getUsername();
                if (username != null) {
                    dos.writeUTF(send + "#" + username.trim());
                    dos.flush();
                    send = replaceEmoji(send); // Reaplce Emoji
                    System.out.println(send);
                    messegesRightTablePane(send);
                    txtMessages.setText("");
                }
            }
        } catch (IOException | BadLocationException ex) {
        }
    }//GEN-LAST:event_btnSendMessageActionPerformed

    private void btnEmojiMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmojiMousePressed
        isOpenEmojiPanel = true;
        pnlMenu.setSize(0, 0);
        lblLeftArrow.setSize(0, 0);
        lblMenu.setSize(40, 40); //
        pnlLogoApp.setSize(0, 0); //
        pnlEmojiMain.setSize(250, 410); //
    }//GEN-LAST:event_btnEmojiMousePressed

    private void btnEmojiExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmojiExitActionPerformed
        if (isOpenEmojiPanel) {
            pnlLogoApp.setSize(250, 450);
            pnlEmojiMain.setSize(0, 410);
        }
        isOpenEmojiPanel = false;
    }//GEN-LAST:event_btnEmojiExitActionPerformed

    private void lbl1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl1MousePressed
        txtMessages.append(" " + emoji.get(0).getUnicode() + " ");
    }//GEN-LAST:event_lbl1MousePressed

    private void lbl2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl2MousePressed
        txtMessages.append(" " + emoji.get(1).getUnicode() + " ");
    }//GEN-LAST:event_lbl2MousePressed

    private void lbl3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl3MousePressed
        txtMessages.append(" " + emoji.get(2).getUnicode() + " ");
    }//GEN-LAST:event_lbl3MousePressed

    private void lbl4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl4MousePressed
        txtMessages.append(" " + emoji.get(3).getUnicode() + " ");
    }//GEN-LAST:event_lbl4MousePressed

    private void lbl5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl5MousePressed
        txtMessages.append(" " + emoji.get(4).getUnicode() + " ");
    }//GEN-LAST:event_lbl5MousePressed

    private void lbl6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl6MousePressed
        txtMessages.append(" " + emoji.get(5).getUnicode() + " ");
    }//GEN-LAST:event_lbl6MousePressed

    private void lbl7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl7MousePressed
        txtMessages.append(" " + emoji.get(6).getUnicode() + " ");
    }//GEN-LAST:event_lbl7MousePressed

    private void lbl8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl8MousePressed
        txtMessages.append(" " + emoji.get(7).getUnicode() + " ");
    }//GEN-LAST:event_lbl8MousePressed

    private void lbl9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl9MousePressed
        txtMessages.append(" " + emoji.get(8).getUnicode() + " ");
    }//GEN-LAST:event_lbl9MousePressed

    private void lbl10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl10MousePressed
        txtMessages.append(" " + emoji.get(9).getUnicode() + " ");
    }//GEN-LAST:event_lbl10MousePressed

    private void lbl11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl11MousePressed
        txtMessages.append(" " + emoji.get(10).getUnicode() + " ");
    }//GEN-LAST:event_lbl11MousePressed

    private void lbl12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl12MousePressed
        txtMessages.append(" " + emoji.get(11).getUnicode() + " ");
    }//GEN-LAST:event_lbl12MousePressed

    private void lbl13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl13MousePressed
        txtMessages.append(" " + emoji.get(12).getUnicode() + " ");
    }//GEN-LAST:event_lbl13MousePressed

    private void lbl14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl14MousePressed
        txtMessages.append(" " + emoji.get(13).getUnicode() + " ");
    }//GEN-LAST:event_lbl14MousePressed

    private void lbl15MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl15MousePressed
        txtMessages.append(" " + emoji.get(14).getUnicode() + " ");
    }//GEN-LAST:event_lbl15MousePressed

    private String replaceEmoji(String send) {
        if (send.contains(":smile:")) {
            send = send.replaceAll(":smile:", emoji.get(0).getUnicode());
        }
        if (send.contains(":smiley:")) {
            send = send.replaceAll(":smiley:", emoji.get(1).getUnicode());
        }
        if (send.contains(":grinning:")) {
            send = send.replaceAll(":grinning:", emoji.get(2).getUnicode());
        }
        if (send.contains(":blush:")) {
            send = send.replaceAll(":blush:", emoji.get(3).getUnicode());
        }
        if (send.contains(":relaxed:")) {
            send = send.replaceAll(":relaxed:", emoji.get(4).getUnicode());
        }
        if (send.contains(":wink:")) {
            send = send.replaceAll(":wink:", emoji.get(5).getUnicode());
        }
        if (send.contains(":heart_eyes:")) {
            send = send.replaceAll(":heart_eyes:", emoji.get(6).getUnicode());
        }
        if (send.contains(":kissing_heart:")) {
            send = send.replaceAll(":kissing_heart:", emoji.get(7).getUnicode());
        }
        if (send.contains(":kissing_closed_eyes:")) {
            send = send.replaceAll(":kissing_closed_eyes:", emoji.get(8).getUnicode());
        }
        if (send.contains(":kissing:")) {
            send = send.replaceAll(":kissing:", emoji.get(9).getUnicode());
        }
        if (send.contains(":kissing_smiling_eyes:")) {
            send = send.replaceAll(":kissing_smiling_eyes:", emoji.get(10).getUnicode());
        }
        if (send.contains(":stuck_out_tongue_winking_eye:")) {
            send = send.replaceAll(":stuck_out_tongue_winking_eye:", emoji.get(11).getUnicode());
        }
        if (send.contains(":stuck_out_tongue_closed_eyes:")) {
            send = send.replaceAll(":stuck_out_tongue_closed_eyes:", emoji.get(12).getUnicode());
        }
        if (send.contains(":stuck_out_tongue:")) {
            send = send.replaceAll(":stuck_out_tongue:", emoji.get(13).getUnicode());
        }
        if (send.contains(":flushed:")) {
            send = send.replaceAll(":flushed:", emoji.get(14).getUnicode());
        }
        return send;
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatApplication.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatApplication().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCamera;
    private javax.swing.JButton btnEmoji;
    private javax.swing.JButton btnEmojiExit;
    private javax.swing.JButton btnSendFile;
    private javax.swing.JButton btnSendMessage;
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl10;
    private javax.swing.JLabel lbl11;
    private javax.swing.JLabel lbl12;
    private javax.swing.JLabel lbl13;
    private javax.swing.JLabel lbl14;
    private javax.swing.JLabel lbl15;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lbl4;
    private javax.swing.JLabel lbl5;
    private javax.swing.JLabel lbl6;
    private javax.swing.JLabel lbl7;
    private javax.swing.JLabel lbl8;
    private javax.swing.JLabel lbl9;
    private javax.swing.JLabel lblCollections;
    private javax.swing.JLabel lblCollections1;
    private javax.swing.JLabel lblContact;
    private javax.swing.JLabel lblEmoji;
    private javax.swing.JLabel lblExit;
    private javax.swing.JLabel lblFullName;
    private javax.swing.JLabel lblFullnameWelcome;
    private javax.swing.JLabel lblHome;
    private javax.swing.JLabel lblHomeHeader;
    private javax.swing.JLabel lblLeftArrow;
    private javax.swing.JLabel lblLogoAppBody;
    private javax.swing.JLabel lblLogoAppHeader;
    private javax.swing.JLabel lblLogoBody;
    private javax.swing.JLabel lblMenu;
    private javax.swing.JLabel lblTextChat;
    private javax.swing.JLabel lblTextCollections1;
    private javax.swing.JLabel lblTextHome;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JList<ClientDTO> listContact;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlChat;
    private javax.swing.JPanel pnlChatContent;
    private javax.swing.JPanel pnlChatDemo;
    private javax.swing.JPanel pnlCollection;
    private javax.swing.JPanel pnlCollectionsDemo;
    private javax.swing.JPanel pnlComposeMessage;
    private javax.swing.JPanel pnlContact;
    private javax.swing.JPanel pnlEmojiBody;
    private javax.swing.JPanel pnlEmojiHeader;
    private javax.swing.JPanel pnlEmojiMain;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlHomeDemo;
    private javax.swing.JPanel pnlLogo;
    private javax.swing.JPanel pnlLogoApp;
    private javax.swing.JPanel pnlLogoAppBody;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JPanel pnlSendEvent;
    private javax.swing.JScrollPane scrollListContact;
    private javax.swing.JScrollPane scrollMesseges;
    private javax.swing.JTextPane txtMessage;
    private javax.swing.JTextArea txtMessages;
    // End of variables declaration//GEN-END:variables
}
