import javax.swing.*;
import java.awt.event.*;

public class DialogoDirector extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nombreTxt;

    private String nombre;
    private boolean okClick = false;//indica si se ha hecho click en OK

    public DialogoDirector() {
        setContentPane(contentPane);
        setModal(true);
        setSize(800, 150);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okClick = true;
                setNombre(nombreTxt.getText());
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
        dispose();
    }
    private void onCancel() {
        dispose();
    }


    public boolean isOkClick() {
        return okClick;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setTextNombreTxt(String nombre) {
        this.nombreTxt.setText(nombre);
    }




    public static void main(String[] args) {
    }
}
