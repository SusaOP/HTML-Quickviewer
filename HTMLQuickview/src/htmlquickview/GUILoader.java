package htmlquickview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class GUILoader {
    public static List<String> filesCreated = new ArrayList<String>();
    public static List<String> filenames = new ArrayList<String>();
    private static JFrame mainF;
    private static final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int WIDTH = (int)(size.width * 0.5);
    private static final int HEIGHT = (int)(size.height * 0.5);

    private static Hashing hasher;
    private static String savePath;
    private static String dataPath;

    public GUILoader(){
        hasher = new Hashing();

        initPaths();
        init_mainF();
        initGUI();
    }

    // initialize the GUI interface
    public static void initGUI(){

        JPanel panelButtons = new JPanel(new FlowLayout());	//button panel

        // text area for comments input
        JTextArea inputJTA = new JTextArea("", 30, 110);
        inputJTA.setLineWrap(true);
        inputJTA.setWrapStyleWord(true);
        inputJTA.setMinimumSize(new Dimension(250,400));

        JScrollPane scrollPane = new JScrollPane(inputJTA);

        // button to submit comments (<= 100 chars)
        JButton submitB = new JButton("Submit");
        submitB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String htmlInput = inputJTA.getText();

                //System.out.println("HTML input is:\n"+htmlInput);
                writeHTMLFile(htmlInput);

                inputJTA.setText("");
            }
        });

        panelButtons.add(submitB);

        //mainF.add(scrollPane);
        mainF.getContentPane().add(scrollPane, BorderLayout.CENTER);
        mainF.getContentPane().add(panelButtons, BorderLayout.SOUTH);
        mainF.pack();
        mainF.setVisible(true);

    }

    //set mainF for the entire class
    public static void init_mainF(){
        mainF = new JFrame("HTMLQuickview");
        //set as do nothing to enable a popup window on close
        mainF.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainF.setSize(WIDTH,HEIGHT);

        mainF.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent){

                //list the names of the files to the user during save confirmation

                //Create list model
                DefaultListModel<String> model = new DefaultListModel<>();

                //Add files to the list model
                for (String file : filenames){
                    model.addElement(file);
                }

                //Create a JList
                JList<String> jList = new JList<>(model);
                //Create a JScrollPane to hold the JList
                JScrollPane exitScrollPane = new JScrollPane(jList);
                //Create a JPanel to hold the scroll pane
                JPanel exitPanel = new JPanel();

                exitPanel.add(exitScrollPane);

                //confirmation page to save or delete files
                int confirm = JOptionPane.showConfirmDialog(mainF, exitPanel, "Save Confirmation",
                        JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                // if Yes to saving files
                manageFiles(confirm == JOptionPane.YES_OPTION);
                mainF.dispose();

            }
        });
    }

    //write html data to file
    public static void writeHTMLFile(String html_str){

        String filename = hasher.hashString(html_str) + ".html";
        filenames.add(filename);
        //get current working directory
        String fullFilePath = dataPath+filename;
        filesCreated.add(fullFilePath);

        //System.out.println("Current Directory is: "+curDir );


        try{
            FileWriter fileWriter = new FileWriter(fullFilePath,true);

            // Create a BufferedWriter object to wrap the FileWriter object
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write the data to the file
            bufferedWriter.write(html_str);

            // Flush the buffered data to the file and close the writer
            bufferedWriter.flush();
            bufferedWriter.close();

            openFile(fullFilePath);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //open html file in firefox
    public static void openFile(String filePath){
        try{
            Process process = Runtime.getRuntime().exec("firefox "+ filePath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //initialize common paths
    public static void initPaths(){
        dataPath = System.getProperty("user.dir")+"/data/";
        savePath = dataPath+"Saved/";
    }

    //save or delete files - called on exiting program
    public static void manageFiles(boolean saveFiles){

        if (saveFiles){
            //save files
            for (String existingFilePath : filesCreated){
                try{
                    Process process = Runtime.getRuntime().exec("mv "+ existingFilePath + " " + savePath);

                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            System.out.println("Saved:");
        } else{
            //delete files
            for (String existingFilePath : filesCreated) {
                try {
                    Process process = Runtime.getRuntime().exec("rm " + existingFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Deleted:");
        }
        //print files that were saved or deleted
        for (String name : filenames){
            System.out.println("\t"+name);
        }
    }


}

























