import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.text.NumberFormat;
import java.util.Locale;

class Calculator extends JFrame implements ActionListener {
    private double firstNumber = 0;
    private String operator = "";
    private boolean isNewInput = true;
    private double maxValue = 999999999;
    private double minValue = -999999999;
    NumberFormat locale = NumberFormat.getInstance(Locale.KOREA);

    JLabel ResultLabel = new JLabel("0");

    public Calculator(String title) {
        super(title);
        setLayout(new BorderLayout());
        setSize(260, 400);
        setIconImage(new ImageIcon(getClass().getResource("/icon.jpg")).getImage());

        JPanel ResultPanel = new JPanel(new BorderLayout());
        ResultPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 15));
        ResultLabel.setFont(ResultLabel.getFont().deriveFont(32f));
        ResultPanel.add(ResultLabel, BorderLayout.EAST);
        add(ResultPanel, BorderLayout.NORTH);

        JPanel KeypadPanel = new JPanel(new GridLayout(0, 4));
        add(KeypadPanel, BorderLayout.CENTER);

        String[] keypads = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "C", "0", "=", "+",
        };

        for (String key : keypads) {
            JButton btn = new JButton(key);
            btn.addActionListener(this);
            KeypadPanel.add(btn);
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setResultLabel(double result) {
        if (result > maxValue) result = maxValue;
        else if (result < minValue) result = minValue;
        ResultLabel.setText(formatResult(result));
    }

    public double getResultLabel() {
        try {
            return locale.parse(ResultLabel.getText()).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("C")) {
            firstNumber = 0;
            operator = "";
            isNewInput = true;
            ResultLabel.setText("0");

        } else if (cmd.matches("[0-9]")) {
            if (isNewInput) {
                ResultLabel.setText(cmd);
                isNewInput = false;
            } else {
                String current = ResultLabel.getText().replace(",", "");
                String raw = current.equals("0") ? cmd : current + cmd;
                long parsed = Long.parseLong(raw);
                if (parsed > maxValue) parsed = (long) maxValue;
                ResultLabel.setText(locale.format(parsed));
            }

        } else if (cmd.equals("=")) {
            if (!operator.isEmpty()) {
                double secondNumber = getResultLabel();
                double result = calculate(firstNumber, secondNumber, operator);
                setResultLabel(result);
                operator = "";
                isNewInput = true;
            }

        } else {
            if (!operator.isEmpty() && !isNewInput) {
                double secondNumber = getResultLabel();
                firstNumber = calculate(firstNumber, secondNumber, operator);
                setResultLabel(firstNumber);
            } else {
                firstNumber = getResultLabel();
            }
            operator = cmd;
            isNewInput = true;
        }
    }

    private String formatResult(double result) {
        if (result == Math.floor(result) && !Double.isInfinite(result)) {
            return locale.format((long) result);
        }
        return locale.format(result);
    }

    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0) {
                    ResultLabel.setText("오류");
                    operator = "";
                    isNewInput = true;
                    return 0;
                }
                return a / b;
            default: return b;
        }
    }
}