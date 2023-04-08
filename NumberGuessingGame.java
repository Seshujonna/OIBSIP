import java.awt.*;  
import javax.swing.*;
import java.awt.event.*;  
import java.util.Random;

class NumberGuess{
    JFrame frame;
    JLabel label;
    JButton button;
    JTextField field;
    JLabel result;
    JLabel cnt;
    public String st="";
    int number;
    int count=0;

    NumberGuess(){

        Random random = new Random();
        number = random.nextInt(1,101);
        
        frame= new JFrame("Number Guessing Game"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(570,200);       
        frame.setSize(400,300);  
        
        label = new JLabel("Guess the number: ");
        label.setBounds(20,30,400,50);
        label.setFont(new Font("Ariel",Font.BOLD,20));
        frame.add(label);

        field = new JTextField();
        field.setBounds(210,45,150,25);
        field.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        frame.add(field);
        

        button = new JButton("Check");
        button.setBounds(150,100,80,20);
        frame.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = field.getText();
                
                int val = Integer.parseInt(value);

                if(val>100){
                    st="Please enter a number less than 100";
                    --count;
                }
                else if(val==number){
                    st="Congrats!!! Your guess is correct";
                }
                else if(val>number){
                    st="Your number is high";
                }
                else if(val<number){
                    st="Your number is low";
                }
                ++count;
                result.setText(st);
                if(st=="Congrats!!! Your guess is correct"){
                    cnt.setText("You guessed correct in "+Integer.toString(count)+" trails");
                    result.setForeground(Color.GREEN);
                }
        }});
        frame.getRootPane().setDefaultButton(button);

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        result = new JLabel("",SwingConstants.CENTER);
        result.setBounds(0,130,400,50);
        result.setFont(new Font("Ariel",Font.BOLD,15));
        frame.add(result);

        cnt = new JLabel("",SwingConstants.CENTER);
        cnt.setBounds(0,170,400,50);
        cnt.setFont(new Font("Ariel",Font.BOLD,15));
        frame.add(cnt);
        
        frame.setLayout(null);    
        frame.setVisible(true);
    }


}


public class NumberGuessingGame {

    public static void main(String[] args) {

        new NumberGuess();

    }

}
