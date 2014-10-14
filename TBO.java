import java.io.*;
import java.util.*;

public class TBO {
    
    private int[][] prod;
    private int proddcount;
    private Vector<String> varname = new Vector<>();
    private Vector<String> constname = new Vector<>();
    private boolean tab[][][];
    private Vector<String> word = new Vector<>();
    
    private String[] lines = new String[100];
    private int lastLine;
    
    public TBO(){
        lastLine=0;
    }
    
    public void readText() throws IOException{
        BufferedReader f = null;
        try {
            f = new BufferedReader(new FileReader("test"));
            String s1;
            while ((s1 = f.readLine()) != null){
                lastLine++;
                lines[lastLine]=s1;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("nf");
        } catch (SecurityException ex){
            System.out.println("sec");
        }finally {
            try {
                f.close();
            } catch (IOException ex) {
                
            }
        }
    }
    
    public void showText(){
        for (int j=1; j<=lastLine; j++){
            System.out.println(lines[j]);
        }
    }
    
    public void init() throws IOException{
        BufferedReader f = null;
        // init varname
        try {
            f = new BufferedReader(new FileReader("cnf"));
            String s1;
            while ((s1 = f.readLine()) != null){
                StringTokenizer st = new StringTokenizer(s1);
                varname.add(st.nextToken());
            }
        } catch (FileNotFoundException ex) {
            System.out.println("nf");
        } catch (SecurityException ex){
            System.out.println("sec");
        }finally {
            try {
                f.close();
            } catch (IOException ex) {
                
            }
        }
        // init prod
        prod = new int[1000][3];
        prodcount = 0;
        int varcount = 0;
        try {
            f = new BufferedReader(new FileReader("cnf"));
            String s1;
            while ((s1 = f.readLine()) != null){
                StringTokenizer st = new StringTokenizer(s1);
                String s = st.nextToken();
                String t = st.nextToken();
                while (st.hasMoreTokens()){
                    s = st.nextToken();
                    if (st.hasMoreTokens()){
                        t = st.nextToken();
                        if (t.charAt(0)=='|'){
                            int i;
                            for (i=0; i<constname.size(); i++){
                                if (constname.elementAt(i).equals(s)) break;
                            }
                            if (i>=constname.size()){
                                constname.add(s);
                            }
                            prod[prodcount][0] = varcount;
                            prod[prodcount][1] = i;
                            prod[prodcount++][2] = -1;
                        } else {
                            int i,j;
                            for (i=0; i<varname.size(); i++){
                                if (varname.elementAt(i).equals(s)) break;
                            }
                            for (j=0; j<varname.size(); j++){
                                if (varname.elementAt(j).equals(t)) break;
                            }
                            prod[prodcount][0] = varcount;
                            prod[prodcount][1] = i;
                            prod[prodcount++][2] = j;
                            if (st.hasMoreTokens()){
                                s = st.nextToken();
                            }
                        }
                    } else {
                        int i;
                        for (i=0; i<constname.size(); i++){
                            if (constname.elementAt(i).equals(s)) break;
                        }
                        if (i>=constname.size()){
                            constname.add(s);
                        }
                        prod[prodcount][0] = varcount;
                        prod[prodcount][1] = i;
                        prod[prodcount++][2] = -1;
                    }
                }
                varcount++;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("nf");
        } catch (SecurityException ex){
            System.out.println("sec");
        }finally {
            try {
                f.close();
            } catch (IOException ex) {
                
            }
        }
        // init word
        for (int i=1; i<=lastLine; i++){
            StringTokenizer st = new StringTokenizer(lines[i]);
            String s;
            int c;
            while (st.hasMoreTokens()){
                s = st.nextToken();
                for (c=0; c<constname.size(); c++){
                    if (constname.elementAt(c).equals(s)) break;
                }
                if (c==constname.size()){
                    for (int l=0; l<s.length(); l++){
                        word.add(s.substring(l, l+1));
                    }
                } else {
                    word.add(s);
                }
            }
        }
        tab = new boolean [word.size()][word.size()][varname.size()];
        for (int i=0; i<word.size(); i++){
            for (int j=0; j<word.size(); j++){
                for (int k=0; k<varname.size(); k++){
                    tab[i][j][k]= false;
                }
            }
        }
        for (int i=0; i<word.size(); i++){
            for (int j=0; j<constname.size(); j++){
                if (constname.elementAt(j).equals(word.elementAt(i))){
                    for (int k=0; k<prodcount; k++){
                        if (prod[k][1]==j && prod[k][2]==-1){
                            tab[i][0][prod[k][0]] = true;
                        }
                    }
                }
            }
        }
        
        /*
        for (int i=0; i<varname.size(); i++){
            System.out.println(i+" "+varname.elementAt(i));
        }
        for (int i=0; i<constname.size(); i++){
            System.out.println(i+" "+constname.elementAt(i));
        }
        for (int i=0; i<prodcount; i++){
            System.out.print(i+". ");
            for (int j=0; j<3; j++){
                System.out.print(j+". "+prod[i][j]+" ");
            }
            System.out.println();
        }
        for (int i=0; i<word.size(); i++){
            System.out.println(i+" "+word.elementAt(i));
        }
        for (int i=0; i<word.size(); i++){
            for (int j=0; j<word.size(); j++){
                for (int k=0; k<varname.size(); k++){
                    if (tab[i][j][k])
                    System.out.println(i+" "+j+" "+k+" ");
                }
            }
        }
        */
    }
    
    public void cyk(){
        for (int i=1; i<word.size(); i++){
            for (int j=0; j<word.size()-i; j++){
                for (int k=0; k<i; k++){
                    for (int l=0; l<prodcount; l++){
                        //System.out.print(i+" "+j+" "+k+" "+l+"\n");
                        if (prod[l][2] >= 0){
                            if ((tab[j][k][prod[l][1]]) && (tab[j+k+1][i-k-1][prod[l][2]])){
                                tab[j][i][prod[l][0]] = true;
                            }
                        }
                    }
                }
            }
        }
        boolean ok = false;
        for (int i=0; i<prodcount; i++){
            if (varname.elementAt(prod[i][0]).equalsIgnoreCase("S")){
                if (tab[0][word.size()-1][i]){
                    ok = true;
                    break;
                }
            }
        }
        if (ok) {
            System.out.println("OK, Valid");
        } else {
            System.out.println("Error at line ");
        }
    }
    
    public static void main (String [] args) throws IOException {
        TBO t = new TBO();
        t.readText();
        t.init();
        t.cyk();
        System.exit(0);
    }
    
}
