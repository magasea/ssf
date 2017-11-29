package com.shellshellfish.datamanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class AccountServiceApplication {

	public static void main(String[] args) {
		/*
		BufferedReader bf;
        bf= null;  
        Process proc = null;  
        try{  
            
        	proc =Runtime.getRuntime().exec("./getallfundcodes.sh",null,new File("/work/wtwong/workspace/choice/api"));
        	int abc=proc.waitFor();
        	int exitcode=proc.exitValue();
        	
        	if (exitcode!=0)
        	   bf = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        	//p = run.exec(command,null,new File("/work/wtwong/workspace/choice/api"));  
        	else
        		bf = new BufferedReader(new InputStreamReader(proc.getInputStream()));  
            
        	String msg = null;  
            
            while((msg = bf.readLine()) != null){  
                System.out.println(msg);  
            }  
            System.out.println("exit code:"+exitcode);
            
            
        }catch(Exception e){  
            e.printStackTrace();  
        }finally{  
            if(bf != null){  
                  
                try {  
                    bf.close();  
                    proc.destroy();  
                } catch (IOException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }
            
        }  
        */
		SpringApplication.run(AccountServiceApplication.class, args);
	}
	
	
     	
}
