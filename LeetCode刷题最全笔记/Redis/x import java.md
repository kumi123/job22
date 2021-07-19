```
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Main {


/*请完成下面这个函数，实现题目要求的功能
当然，你也可以不按照下面这个模板来作答，完全按照自己的想法来 ^-^ 
******************************开始写代码******************************/
    static String buildingHouse(String n) {
        if(n.charAt(0)-'0'==1) return new String("R");
        else if(n.charAt(0)-'0'==2) return new String("GRR");
        else if(n.charAt(0)-'0'>=2&&n.charAt(0)-'0'<=12){
        String pp="GRR";
        for(int i=3;i<=n.charAt(0)-'0';i++){
            int m=1<<i;
            //需要有上边的值
            char[] tem=new char[m-1];//存储当前的值
            //先来利用前边的值进行填充
            for(int j=0;j<pp.length();j++){
                tem[2*j+1]=pp.charAt(j);
            }
			int count=0;
            for(int t=0;t<m-1;t+=2){
                if(count%2==0) tem[t]='G';
                else tem[t]='R';    
				count++;
 }
             pp = new String(tem);
            return pp;
 
 
            }
            //System.out.println(res);
        }
        else  return new String("O");
       
        
       

            
            
        }


    }
/******************************结束写代码******************************/


    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String res;
            
        String _n;
        try {
            _n = in.nextLine();
        } catch (Exception e) {
            _n = null;
        }
  
        res = buildingHouse(_n);
        System.out.println(res);
    }
}




import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        if(n==1) System.out.println("R");
        else if(n==2) System.out.println("GRR");
        else if(n>2&&n<=12){
        String pp="GRR";
        for(int i=3;i<=n;i++){
            int m=1<<i;
            //需要有上边的值
            char[] tem=new char[m-1];//存储当前的值
            //先来利用前边的值进行填充
            for(int j=0;j<pp.length();j++){
                tem[2*j+1]=pp.charAt(j);
            }
			int count=0;
            for(int t=0;t<m-1;t+=2){
                if(count%2==0) tem[t]='G';
                else tem[t]='R';    
				count++;
 }
            pp = new String(tem);
            System.out.println(pp);
 
 
            }
            //System.out.println(res);
        }
        else  System.out.println("O");
    }



    }
/******************************结束写代码******************************/




```

