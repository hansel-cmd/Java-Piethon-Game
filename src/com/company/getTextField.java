package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class getTextField extends JFrame {
    JPanel jp = new JPanel();
    JLabel jl = new JLabel("You set a new Record! What is your Piethon's Name?");
    JLabel jl2 = new JLabel();
    JTextField jt = new JTextField(28);
    JButton jb = new JButton("Ok");
    String name = "";
    CompletableFuture<String> anaDataPromise = new CompletableFuture<>();
    Future<String> myFuture;
    public getTextField() {}

    public boolean Clicked() {
        return (name.isEmpty()) ? false : true;
    }
   public void niceMe() {
       setTitle("New High Score!");
       setVisible(true);
       setSize(350, 140);

       Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
       this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

       jp.add(jl);
       jp.add(jt);
       jp.add(jb);
       jp.add(jl2);


       jt.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               name = jt.getText();
           }
       });

       jb.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               name = jt.getText();
               setVisible(false);
           }
       });


       add(jp);
   }



}