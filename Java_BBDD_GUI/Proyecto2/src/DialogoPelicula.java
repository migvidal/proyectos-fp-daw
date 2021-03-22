import javax.swing.*;
import java.awt.event.*;

public class DialogoPelicula extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tituloTxt;
    private JComboBox directorCombo;
    private JTextField paisTxt;
    private JTextField duracionTxt;
    private JTextField generoTxt;

    private JTextField[] textFields = {tituloTxt, paisTxt, duracionTxt, generoTxt};
    private String[] valoresTxt;

    private boolean okClick = false;//indica si se ha hecho click en OK





    //CONSTRUCTOR
    public DialogoPelicula() {

        setContentPane(contentPane);
        setSize(1000, 150);
        setLocationRelativeTo(null);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okClick = true;
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        //call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        //call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    //MÉTODOS
    private void onOK() {
        valoresTxt = new String[]{getTextoTituloTxt(), getTextoPaisTxt(), getTextoDuracionTxt(), getTextoGeneroTxt()};
        dispose();
    }
    private void onCancel() {
        //add your code here if necessary
        dispose();
    }

    public boolean isOkClick() {
        return okClick;
    }
    public String getTextoTituloTxt() {
        return tituloTxt.getText();
    }
    public String getTxtDirectorCombo() {
        return (String) directorCombo.getSelectedItem();
    }
    public int getIndexDirectorCombo() {
        return directorCombo.getSelectedIndex();
    }
    public void setDirectorCombo(DefaultComboBoxModel modelo, int indice) {
        directorCombo.setModel(modelo);
        directorCombo.setSelectedIndex(indice);
    }
    public String getTextoPaisTxt() {
        return paisTxt.getText();
    }
    public String getTextoDuracionTxt() {
        return duracionTxt.getText();
    }
    public String getTextoGeneroTxt() {
        return generoTxt.getText();
    }
    public String[] getValoresTxt() {
        return valoresTxt;
    }
    public void setTextFieldsTexto(String[] valores) {
        for (int i=0; i<valores.length; i++) {
            textFields[i].setText(valores[i]);
        }
    }


    

    public static void main(String[] args) {

    }
}
