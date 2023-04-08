import java.awt.*;  
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Random;

import de.wannawork.jcalendar.*;
import java.sql.*;

//Startup page
class StartPage{
    StartPage()  
    {
        //Adding frame
        JFrame frame= new JFrame("Online Reservation System"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(570,200); 
        frame.setSize(400,300);

        //Heading section
        JLabel heading = new JLabel("<html>Welcome to Oasis Infobyte<br/>Online Reservation System<br/></html>");
        heading.setBounds(65,20,400,100);
        heading.setFont(new Font("Ariel",Font.BOLD,20));
        frame.add(heading); 

        //Login and signup button section
        JButton login_button = new JButton("Login");
        login_button.setBounds(100,150,80,20);
        frame.add(login_button);
        frame.getRootPane().setDefaultButton(login_button);

        JButton signup_button = new JButton("Signup");
        signup_button.setBounds(200,150,80,20);    
        frame.add(signup_button);

        //Login button Action listener
        login_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage();
                frame.setVisible(false);
            }
        });
        login_button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //Signup button Action listener
        signup_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignupPage();
                frame.setVisible(false);
            }
        });
        signup_button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.setLayout(null);    
        frame.setVisible(true);
    }  
}
//Login page
class LoginPage{
    String entered_mail; 
    LoginPage(){
        //Adding frame
        JFrame frame= new JFrame("Login Form"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450,300);
        frame.setLocation(545,200); 

        //Heading section
        JLabel heading = new JLabel("Enter your credentials to login");
        heading.setBounds(100,0,400,90);
        heading.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(heading);

        //Username label and field section
        JLabel username = new JLabel("Email Id: ");
        username.setBounds(65,40,400,100);
        username.setFont(new Font("Ariel",Font.BOLD,16)); 
        frame.add(username);

        //Adding username field
        JTextField username_field = new JTextField();
        username_field.setBounds(150,80,210,25);
        username_field.setFont(new Font("Ariel",Font.PLAIN,14));
        username.setLabelFor(username_field);
        frame.add(username_field);
        entered_mail = username_field.getText();

        //Password label and field section
        JLabel password = new JLabel("Password: ");
        password.setBounds(65,80,400,100);
        password.setFont(new Font("Ariel",Font.BOLD,16)); 
        frame.add(password);

        //Adding password field
        final JPasswordField password_field = new JPasswordField();
        password_field.setBounds(150,120,210,25);
        password_field.setFont(new Font("Ariel",Font.PLAIN,14));
        password.setLabelFor(password_field);
        password_field.setEchoChar('*');
        frame.add(password_field);

        //"Show password" label and Checkbox for displaying password
        JLabel showpwd = new JLabel("Show password");
        showpwd.setBounds(170,110,400,100);
        showpwd.setFont(new Font("Ariel",Font.BOLD,13)); 
        frame.add(showpwd);
        JCheckBox chk = new JCheckBox();
        chk.setBounds(150,150,20,20);
        frame.add(chk);

        //Adding keyListener and action listener to the password field
        //The password is shown or kept hidden based on the checkbox checked or unchecked
        //If the user is typing in the password field, the password automatically hides itself
        password_field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(chk.isSelected()){
                    chk.doClick();
                }
            }
        });
        chk.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(chk.isSelected()){
                    password_field.setEchoChar((char)0);
                }
                else{
                    password_field.setEchoChar('*');
                }          
            }
        });

        //Submit button
        JButton submit_button = new JButton("Submit");
        submit_button.setBounds(180,200,80,20);
        frame.add(submit_button);        
        submit_button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.getRootPane().setDefaultButton(submit_button);
        submit_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String uname = username_field.getText();  
                String pwd = new String(password_field.getPassword()); 
                
                

                if(uname.equals("")){
                    JOptionPane.showMessageDialog(frame, "Please enter username");
                }
                else if(pwd.equals("")){
                    JOptionPane.showMessageDialog(frame, "Please enter password");
                }
                else{
                    String url = "jdbc:mysql://localhost:3306/reservationsystem";
                    String user = "root";
                    String pass = "Seshu@161102";

                    try{
                
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection connection = DriverManager.getConnection(url,user,pass);
                        
                        PreparedStatement em_stmt = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM passenger WHERE email = ?);");
                        em_stmt.setString(1,uname);
                        ResultSet user_res = em_stmt.executeQuery();
                        user_res.next();
                        Boolean bool = user_res.getBoolean(1);

                        if(bool==false){
                            JOptionPane.showMessageDialog(frame, "Email does not exists");
                        }
                        else{
                            PreparedStatement stmt = connection.prepareStatement("SELECT passwd,firstname,lastname,mobno FROM passenger WHERE email = ?;"); 
                            stmt.setString(1,uname);                           
                            ResultSet pwd_res = stmt.executeQuery();
                            if(pwd_res.next() && pwd.equals(pwd_res.getString("passwd"))) {
                                String firstname = pwd_res.getString("firstname");
                                String lastname = pwd_res.getString("lastname");
                                String mobno = pwd_res.getString("mobno");
                                new HomePage(uname, firstname, lastname,mobno);
                                frame.setVisible(false);
                            }
                            else{
                                JOptionPane.showMessageDialog(frame, "Incorrect password");
                            }
                        }
                    }

                    catch (Exception exception) {
                        System.out.println(exception);
                    }
                }

            }
        });
        
        //Back button
        JButton back = new JButton("<html>&#9668</html>");
        back.setBounds(5,5,40,20);
        back.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        frame.add(back);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new StartPage();       
            }
        });

        frame.setLayout(null);    
        frame.setVisible(true);
    }
}
//Signup page
class SignupPage{
    int cnt=0;
    JLabel warning;
    JTextField fname;
    String pw="";
    SignupPage(){
        //Adding frame
        JFrame frame= new JFrame("Signup form"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(470,720);
        frame.setLocation(530,50);

        //Heading section
        JLabel heading = new JLabel("Signup Page");
        heading.setBounds(170,30,200,30);
        heading.setFont(new Font("Ariel",Font.BOLD,20));
        frame.add(heading);

        //First name label and field
        JLabel first_name = new JLabel("First name:");
        first_name.setBounds(50,70,200,30);
        first_name.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(first_name);
        JTextField fname = new JTextField();
        fname.setBounds(50,100,150,25);
        fname.setFont(new Font("Ariel",Font.PLAIN,14));
        frame.add(fname);

        //Last name label and field
        JLabel last_name = new JLabel("Last name:");
        last_name.setBounds(250,70,200,30);
        last_name.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(last_name);
        JTextField lname = new JTextField();
        lname.setBounds(250,100,150,25);
        lname.setFont(new Font("Ariel",Font.PLAIN,14));
        frame.add(lname);

        //Email label and field
        JLabel email_label = new JLabel("Email ID:");
        email_label.setBounds(50,140,70,30);
        email_label.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(email_label);
        JTextField email = new JTextField();
        email.setBounds(50,170,250,25);
        email.setFont(new Font("Ariel",Font.PLAIN,14));
        frame.add(email);

        //Password label and field
        JLabel password_label = new JLabel("Password:");
        password_label.setBounds(50,210,100,30);
        password_label.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(password_label);
        JPasswordField password = new JPasswordField();
        password.setBounds(50,240,250,25);
        password.setFont(new Font("Ariel",Font.PLAIN,14));
        password.setEchoChar('*');
        frame.add(password);

        //Show password field
        JLabel showpwd = new JLabel("Show password");
        showpwd.setBounds(70,230,400,100);
        showpwd.setFont(new Font("Ariel",Font.BOLD,13)); 
        frame.add(showpwd);
        JCheckBox chk = new JCheckBox();
        chk.setBounds(50,270,20,20);
        frame.add(chk);

        //Adding key and action listener
        password.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(chk.isSelected()){
                    chk.doClick();
                }
            }
        });
        chk.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(chk.isSelected()){
                    password.setEchoChar((char)0);
                }
                else{
                    password.setEchoChar('*');
                }          
            }
        });

        //Confirm password label and field
        JLabel confirmpwd_label = new JLabel("Confirm Password:");
        confirmpwd_label.setBounds(50,300,200,30);
        confirmpwd_label.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(confirmpwd_label);

        JPasswordField confirmpwd = new JPasswordField();
        confirmpwd.setBounds(50,330,250,25);
        confirmpwd.setFont(new Font("Ariel",Font.PLAIN,14));
        confirmpwd.setEchoChar('*');
        frame.add(confirmpwd);

        //Gender label and field
        JLabel gender_label = new JLabel("Gender:");
        gender_label.setBounds(50,370,70,30);
        gender_label.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(gender_label);

        String gender[]={" ","Male","Female","Other"};
        JComboBox<String> gen = new JComboBox<>(gender);
        gen.setBounds(120,375,100,25);
        frame.add(gen);

        //Marital status label and field
        JLabel mar_label = new JLabel("Marital status:");
        mar_label.setBounds(50,420,120,30);
        mar_label.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(mar_label);

        String mar[]={" ","Married","Unmarried"};
        JComboBox<String> marriage = new JComboBox<>(mar);
        marriage.setBounds(170,425,100,25);
        frame.add(marriage);

        //Date of Birth
        JLabel dob_label = new JLabel("Date of Birth:");
        dob_label.setBounds(50,470,120,30);
        dob_label.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(dob_label);
        //Creating Calendar combobox for DOB
        JCalendarComboBox calendar = new JCalendarComboBox();
        calendar.setBounds(160,475,150,25);
        frame.add(calendar);
       
        //Mobile number
        JLabel mobileno_label = new JLabel("Mobile no:");
        mobileno_label.setBounds(50,520,200,30);
        mobileno_label.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(mobileno_label);

        JTextField mobileno = new JTextField();
        mobileno.setBounds(50,550,250,25);
        mobileno.setFont(new Font("Ariel",Font.PLAIN,14));
        frame.add(mobileno);
        
        //Create account button
        JButton createacc = new JButton("Submit");
        createacc.setBounds(180,610,90,20);
        frame.add(createacc);
        createacc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.getRootPane().setDefaultButton(createacc);
        createacc.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                String f_name = fname.getText();
                String l_name = lname.getText();
                String e_mail = email.getText();
                String pass_word = new String(password.getPassword());
                String conf_pwd = new String(confirmpwd.getPassword());
                String gender = gen.getSelectedItem().toString();
                String marr = marriage.getSelectedItem().toString();
                String mob_no = mobileno.getText();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
                String date = sdf.format(calendar.getDate());

                if(f_name.equals("") || l_name.equals("") || e_mail.equals("") || 
                pass_word.length()==0 || conf_pwd.length()==0 || gender.equals(" ") || 
                marr.equals(" ") || mob_no.equals(("")) || date.equals(" ")){
                    JOptionPane.showMessageDialog(frame, "Please fill all the requirements");
                }

                else if(!(Arrays.equals(password.getPassword(), confirmpwd.getPassword()))){
                    JOptionPane.showMessageDialog(frame, "Password in both fields must be same");}

                else{
                    String url = "jdbc:mysql://localhost:3306/reservationsystem";
                    String user = "root";
                    String pass = "Seshu@161102";

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection connection = DriverManager.getConnection(url,user,pass);

                        // Check if email already exists
                        String chk = "SELECT EXISTS(SELECT 1 FROM passenger WHERE email = ?)";
                        PreparedStatement chkStmt = connection.prepareStatement(chk);
                        chkStmt.setString(1, e_mail);
                        ResultSet chkRes = chkStmt.executeQuery();
                        chkRes.next();
                        Boolean emailExists = chkRes.getBoolean(1);
                        chkStmt.close();

                        if (emailExists) {
                            JOptionPane.showMessageDialog(frame, "Email already exists");
                        } 
                        else {
                        // Insert new record
                        String sql = "INSERT INTO passenger(firstname, lastname, email, passwd, gender, marriage, dob, mobno) VALUES (?, ?, ?, ?, ?, ?, STR_TO_DATE(?, '%d-%m-%Y'), ?)";
                        PreparedStatement insertStmt = connection.prepareStatement(sql);
                        insertStmt.setString(1, f_name);
                        insertStmt.setString(2, l_name);
                        insertStmt.setString(3, e_mail);
                        insertStmt.setString(4, pass_word);
                        insertStmt.setString(5, gender);
                        insertStmt.setString(6, marr);
                        insertStmt.setString(7, date);
                        insertStmt.setString(8, mob_no);
                        insertStmt.executeUpdate();
                        insertStmt.close();

                        frame.setVisible(false);
                        new AccountCreated();
                        }

                        connection.close();
                    } catch (Exception exception) {
                        System.out.println(exception);
                    }
                }
            }
        });

        warning = new JLabel("",SwingConstants.CENTER);
        warning.setBounds(0,590,100,20);
        warning.setForeground(Color.RED);
        warning.setFont(new Font("Ariel",Font.PLAIN,14));
        frame.add(warning);

        //Back button
        JButton back = new JButton("<html>&#9668</html>");
        back.setBounds(5,5,40,20);
        frame.add(back);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new StartPage();       
            }
        });

        frame.setLayout(null);    
        frame.setVisible(true);
    }
}

class AccountCreated{
    AccountCreated(){
        JFrame frame= new JFrame("Account Created"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(570,200);
        frame.setSize(400,300);
        
        JLabel acc_created = new JLabel("<html>Congrats! Your account is created!<br/><br/>&nbsp;&nbsp;&nbsp;Go to login page for logging in</html>");
        acc_created.setBounds(55,30,300,100);
        acc_created.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(acc_created);

        JButton login = new JButton("Login");
        login.setBounds(150,150,80,20);
        frame.add(login);
        login.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.getRootPane().setDefaultButton(login);
        login.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new LoginPage();       
            }
        });

        frame.setLayout(null);
        frame.setVisible(true);
    }
}

class HomePage{
    HomePage(String uname,String firstname,String lastname,String mobno){
        JFrame frame= new JFrame("Home Page"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);
        frame.setLocation(475,170);

        JLabel greetings = new JLabel("Hello, "+firstname+lastname);
        greetings.setBounds(200,30,200,25);
        greetings.setFont(new Font("Ariel",Font.BOLD,20));
        frame.add(greetings);

        JLabel msg = new JLabel("Where do you like to travel today?");
        msg.setBounds(20,80,400,25);
        msg.setFont(new Font("Ariel",Font.ITALIC,18));
        frame.add(msg);

        JButton bookticket = new JButton("Book ticket");
        bookticket.setBounds(220,140,130,30);
        bookticket.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.add(bookticket);
        
        bookticket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                new BookTicket(uname,firstname,lastname, mobno);
                frame.setVisible(false);
            }
        });

        JButton cancelticket = new JButton("Cancel ticket");
        cancelticket.setBounds(220,200,130,30);
        cancelticket.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.add(cancelticket);

        cancelticket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                new CancelTicket(uname,firstname,lastname,mobno);
                frame.setVisible(false);
            }
        });

        JButton logout = new JButton("Logout");
        logout.setBounds(220,260,130,30);
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.add(logout);

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                new StartPage();
                frame.setVisible(false);
            }
        });

        frame.setLayout(null);
        frame.setVisible(true);
    }
}


class BookTicket{
    BookTicket(String uname,String firstname, String lastname,String mobno){
        JFrame frame = new JFrame("Booking Form");
        frame.setSize(600,600);
        frame.setLocation(475,100);;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel heading = new JLabel("Ticket Booking");
        heading.setBounds(220,30,200,30);
        heading.setFont(new Font("Ariel",Font.BOLD,20));
        frame.add(heading);

        JLabel name = new JLabel("Name: "+firstname+" "+lastname);
        name.setBounds(40,80,200,30);
        name.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(name);

        JLabel email = new JLabel("E-mail: "+uname);
        email.setBounds(40,120,300,30);
        email.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(email);

        JLabel phoneno = new JLabel("Phone no: "+mobno);
        phoneno.setBounds(40,160,300,30);
        phoneno.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(phoneno);

        JLabel trno = new JLabel("Enter train number: ");
        trno.setBounds(40,200,300,30);
        trno.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(trno);

        JTextField train_no = new JTextField();
        train_no.setBounds(200,200,200,30);
        train_no.setFont(new Font("Ariel",Font.PLAIN,16));
        frame.add(train_no);

        JLabel trname = new JLabel("Train name: ");
        trname.setBounds(40,240,300,30);
        trname.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(trname);

        JTextField train_name = new JTextField();
        train_name.setBounds(150,240,200,30);
        train_name.setFont(new Font("Ariel",Font.PLAIN,16));
        frame.add(train_name);

        JLabel classtp = new JLabel("Class type: ");
        classtp.setBounds(40,280,300,30);
        classtp.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(classtp);

        String class_tp[]={" ","EC","1A","2A","3A","FC","CC","SL","2S"};
        JComboBox<String> cl = new JComboBox<>(class_tp);
        cl.setBounds(140,285,100,25);
        frame.add(cl);

        JLabel date = new JLabel("Date of Journey: ");
        date.setBounds(40,320,300,30);
        date.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(date);

        JCalendarComboBox calendar = new JCalendarComboBox();
        calendar.setBounds(180, 325, 150, 25);
        frame.add(calendar);

        JLabel from = new JLabel("From: ");
        from.setBounds(40,370,300,30);
        from.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(from);

        JTextField from_name = new JTextField();
        from_name.setBounds(40,400,200,30);
        from_name.setFont(new Font("Ariel",Font.PLAIN,16));
        frame.add(from_name);

        JLabel to = new JLabel("To: ");
        to.setBounds(280,370,300,30);
        to.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(to);

        JTextField to_name = new JTextField();
        to_name.setBounds(280,400,200,30);
        to_name.setFont(new Font("Ariel",Font.PLAIN,16));
        frame.add(to_name);

        
        train_no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usertrno = train_no.getText();

                String url = "jdbc:mysql://localhost:3306/reservationsystem";
                String user = "root";
                String pass = "Seshu@161102";
                try{ 
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url,user,pass);
                    PreparedStatement stmt = connection.prepareStatement("select trainname from train where trainno=?");
                    stmt.setString(1,usertrno);
                    ResultSet res = stmt.executeQuery();
                    res.next();
                    train_name.setText(res.getString("trainname"));
                }    
                catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        });
        
        JButton bookticket = new JButton("Book ticket");
        bookticket.setBounds(230,470,100,30);
        bookticket.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.getRootPane().setDefaultButton(bookticket);
        frame.add(bookticket);
        bookticket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String tr_no = train_no.getText();
                String tr_name = train_name.getText();
                String class_tp = cl.getSelectedItem().toString();
                String from_ip = from_name.getText();
                String to_ip = to_name.getText();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
                String date = sdf.format(calendar.getDate());
                String pnrno = generatePNR();

                if(tr_no.equals("") || tr_name.equals("") || class_tp.equals(" ") || 
                from_ip.equals(" ") || to_ip.equals(("")) || date.equals(" ")){
                    JOptionPane.showMessageDialog(frame, "Please fill all the requirements");
                }
                else{
                    String url = "jdbc:mysql://localhost:3306/reservationsystem";
                    String user = "root";
                    String pass = "Seshu@161102";

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection connection = DriverManager.getConnection(url,user,pass);

                        String sql = "insert into booking_details(pnr, firstname, lastname, email, trainno, trainname, class, dateofjourney, fromplace, toplace) values (?, ?, ?, ?, ?, ?, ?, STR_TO_DATE(?, '%d-%m-%Y'), ?, ?)";
                        PreparedStatement stmt = connection.prepareStatement(sql);
                        stmt.setString(1, pnrno);
                        stmt.setString(2, firstname);
                        stmt.setString(3, lastname);
                        stmt.setString(4, uname);
                        stmt.setString(5, tr_no);
                        stmt.setString(6, tr_name);
                        stmt.setString(7, class_tp);
                        stmt.setString(8, date);
                        stmt.setString(9, from_ip);
                        stmt.setString(10, to_ip);
                        stmt.executeUpdate();
                        stmt.close();
                        connection.close();

                        frame.setVisible(false);
                        new ConfirmationPage(uname,firstname,lastname,pnrno);
                    }
                    catch(Exception ex){
                    }
                }
            }
        });

        JButton back = new JButton("<html>&#9668</html>");
        back.setBounds(5,5,40,20);
        frame.add(back);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new HomePage(uname, firstname, lastname, mobno);       
            }
        });

        frame.setLayout(null);
        frame.setVisible(true);
    }
    
    private static final int length = 6;
    public static String generatePNR() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
        return builder.toString();
    }
}


class ConfirmationPage{
    ConfirmationPage(String uname, String firstname, String lastname, String pnrno){

        JFrame frame = new JFrame();
        frame.setSize(450,300);
        frame.setLocation(545,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel greetings = new JLabel("Bon Voyage!");
        greetings.setFont(new Font("Ariel",Font.BOLD+Font.ITALIC,30));
        greetings.setBounds(120,30,300,50);
        frame.add(greetings);

        JLabel pnrdisp = new JLabel("Your PNR number is: "+pnrno);
        pnrdisp.setFont(new Font("Ariel",Font.BOLD+Font.ITALIC,20));
        pnrdisp.setBounds(70,80,300,50);
        frame.add(pnrdisp);

        JLabel msg = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Your ticket is booked.<br/> Wishing you a safe and happy Journey.</html>");
        msg.setFont(new Font("Ariel",Font.ITALIC,16));
        msg.setBounds(70,130,500,50);
        frame.add(msg);

        JButton homebutton = new JButton("Home");
        homebutton.setBounds(170,200,80,20);
        homebutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.getRootPane().setDefaultButton(homebutton);
        frame.add(homebutton);
        homebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                new HomePage(uname, firstname, lastname, null);
                frame.setVisible(false);
            }
        });

        frame.setLayout(null);
        frame.setVisible(true);
    }
}

class CancelTicket{
    JLabel details;
    JButton cancel;
    String first_name;
    String last_name;
    String email;
    String userpnr;
    CancelTicket(String uname, String first_name, String last_name, String mobno){
        JFrame frame = new JFrame("Cancelation form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450,450);
        frame.setLocation(550,170);

        JLabel head = new JLabel("Cancelation Form");
        head.setBounds(120,20,300,30);
        head.setFont(new Font("Ariel",Font.BOLD,20));
        frame.add(head);

        JLabel msg = new JLabel("Enter your PNR number: ");
        msg.setBounds(40,60,300,30);
        msg.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(msg);

        JTextField pnr = new JTextField();
        pnr.setBounds(235,63,150,30);
        pnr.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(pnr);
        pnr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                userpnr = pnr.getText();

                if(userpnr.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Please enter PNR number");
                }
                else{
                    String url = "jdbc:mysql://localhost:3306/reservationsystem";
                    String user = "root";
                    String pass = "Seshu@161102";
                    try{ 
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection connection = DriverManager.getConnection(url,user,pass);

                        String chk = "SELECT EXISTS(SELECT 1 FROM booking_details WHERE pnr = ?)";
                        PreparedStatement chkStmt = connection.prepareStatement(chk);
                        chkStmt.setString(1, userpnr);
                        ResultSet chkRes = chkStmt.executeQuery();
                        chkRes.next();
                        Boolean pnrExists = chkRes.getBoolean(1);
                        chkStmt.close();
                        if (!pnrExists) {
                            JOptionPane.showMessageDialog(frame, "No trains were found");
                        } 
                        else{
                            PreparedStatement stmt = connection.prepareStatement("select * from booking_details where pnr=?");
                            stmt.setString(1,userpnr);
                            ResultSet res = stmt.executeQuery();
                            res.next();
                            email = res.getString("email");
                            String train_no = res.getString("trainno");
                            String train_name = res.getString("trainname");
                            String class_tp = res.getString("class");
                            String from = res.getString("fromplace");
                            String to = res.getString("toplace");
                            String date = res.getString("dateofjourney");

                            details.setText("<html>Name: "+first_name+" "+last_name+"<br/>Email: "+email+"<br/>Mobile no: "+mobno+"<br/>Train no: "+train_no+"<br/>Train name: "+train_name+"<br/>Class type: "+class_tp+"<br/>Date: "+date+"<br/>From: "+from+"<br/>To: "+to+"</html>");

                            frame.add(cancel);

                            cancel.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e){
                                    String url = "jdbc:mysql://localhost:3306/reservationsystem";
                                    String user = "root";
                                    String pass = "Seshu@161102";
                            
                                    try {
                                        Class.forName("com.mysql.cj.jdbc.Driver");
                                        Connection connection = DriverManager.getConnection(url,user,pass);
                                        String sql = "delete from booking_details where pnr=?";
                                        PreparedStatement stmt1 = connection.prepareStatement(sql);
                                        stmt1.setString(1, userpnr);
                                        stmt1.executeUpdate();
                                        stmt1.close();
                                        JOptionPane.showMessageDialog(frame, "Ticket Canceled");
                                        new HomePage(email, first_name, last_name, mobno);
                                        frame.setVisible(false);
                                        stmt1.close();
                                        connection.close();
                                    } 
                                    catch (Exception e1) {
                                        System.out.println(e1);
                                    }
                                }
                            }); 

                            stmt.close();
                            connection.close();
                        }
                    }    
                    catch (Exception exception) {
                        System.out.println(exception);
                    }
                }
                
            }
        });

        details = new JLabel("");
        details.setBounds(40,50,300,300);
        details.setFont(new Font("Ariel",Font.BOLD,16));
        frame.add(details);

        cancel = new JButton("Cancel");
        cancel.setBounds(170,320,80,20);
        cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton back = new JButton("<html>&#9668</html>");
        back.setBounds(5,5,40,20);
        frame.add(back);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new HomePage(email, first_name, last_name, mobno);       
            }
        });

        frame.setLayout(null);
        frame.setVisible(true);
    }
}

//Main class
class OnlineReservationSystem{
    public static void main(String args[])  
    {  
        new StartPage();

    }  
} 