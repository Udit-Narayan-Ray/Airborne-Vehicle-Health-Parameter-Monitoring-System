/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itr.cdp;

import java.io.FileReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;



/**
 *
 * @author USER
 */
public class GlgSendData
{
    public static void main(String[] args) throws Exception
    {
          
        try
        {
            int count=0;
            FileReader in=new FileReader("E:\\Intern\\NetBeans Project\\CDP\\src\\main\\java\\com\\itr\\cdp\\in.txt");
            Scanner sc=new Scanner(in);
            int vel=0,pre=0,vol=0;
            InetAddress ip=InetAddress.getByName("127.0.0.1");
            DatagramSocket ds=new DatagramSocket();
            while(sc.hasNextInt())
            {
               if(++count == 101)
               {
                   System.exit(0);
               }
                String str="";
                vel=sc.nextInt();
                pre=sc.nextInt();
                vol=sc.nextInt();
                str=vel+" "+pre+" "+vol;
                System.out.println(str);
                DatagramPacket dp=new DatagramPacket(str.getBytes(),str.length(),ip,9999);
                ds.send(dp);
                
                Thread.sleep(2000);
            }  
            
            ds.close();
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
                
    }
    
}
