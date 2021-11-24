package factory.simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FactorySimulatorGUI extends JPanel implements ActionListener{
    
    private JButton addDispatcher;
    private JButton removeDispatcher;
    private JButton addMachine;
    private JButton removeMachine;
    private JSlider consumptionTime;
    private JSlider productionTime;
    private JLabel dispatchMachineAmt;
    private DrawPanel drawPanel;
    private Timer timer;
    private ArrayList<Dispatcher> dispatchers;
    private ArrayList<Machine> machines;
    private ConveyorBelt[] conveyorBelts;
    private int MAX_COMPONENTS;
    
    public FactorySimulatorGUI() {
        super(new BorderLayout());
        
        MAX_COMPONENTS = 5;
        
        dispatchers = new ArrayList<>(MAX_COMPONENTS);
        machines = new ArrayList<>(MAX_COMPONENTS);
        conveyorBelts = new ConveyorBelt[MAX_COMPONENTS];
        
        for(int i = 0; i < MAX_COMPONENTS; i++) {
            ConveyorBelt newConveyorBelt = new ConveyorBelt(8);
            conveyorBelts[i] = newConveyorBelt;
        }
        
        addDispatcher = new JButton("Add Dispatcher");
        addDispatcher.addActionListener((ActionListener)this);
        
        removeDispatcher = new JButton("Remove Dispatcher");
        removeDispatcher.addActionListener((ActionListener)this);
        
        consumptionTime = new JSlider(JSlider.HORIZONTAL, (int)Machine.MIN_CONSUMPTION_TIME, (int)Machine.MAX_CONSUMPTION_TIME, 1000);
        consumptionTime.addChangeListener(new SliderListener());
        
        addMachine = new JButton("Add Machine");
        addMachine.addActionListener((ActionListener)this);
        
        removeMachine = new JButton("Remove Machine");
        removeMachine.addActionListener((ActionListener)this);
        
        productionTime = new JSlider(JSlider.HORIZONTAL, (int)Machine.MIN_PRODUCTION_TIME, (int)Machine.MAX_PRODUCTION_TIME, 1000);
        productionTime.addChangeListener(new SliderListener());
        
        dispatchMachineAmt = new JLabel("Dispatchers: "+dispatchers.size()+"    Machines: "+machines.size());
        
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.LINE_AXIS));
        southPanel.setPreferredSize(new Dimension(0, 80));
        
        southPanel.add(addDispatcher);
        southPanel.add(removeDispatcher);
        southPanel.add(new JLabel("Max Consumption Time"));
        southPanel.add(consumptionTime);
        southPanel.add(addMachine);
        southPanel.add(removeMachine);
        southPanel.add(new JLabel("Max Production Time"));
        southPanel.add(productionTime);
        southPanel.setBackground(new Color(255, 210, 120));
        
        add(southPanel, BorderLayout.SOUTH);
        
        JPanel northPanel = new JPanel();
        
        northPanel.add(dispatchMachineAmt);
        
        add(northPanel, BorderLayout.NORTH);
        
        drawPanel = new DrawPanel();
        
        add(drawPanel, BorderLayout.CENTER);
        
        timer = new Timer(20,this);
        timer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if(source == addDispatcher) {
            if(dispatchers.size() < MAX_COMPONENTS) {
                Dispatcher newDispatcher = new Dispatcher(conveyorBelts);
                dispatchers.add(newDispatcher);
                Thread dispatcherThread = new Thread(newDispatcher);
                newDispatcher.isAlive = true;
                dispatcherThread.start();
            }
            dispatchMachineAmt.setText("Dispatchers: "+dispatchers.size()+"    Machines: "+machines.size());
        }
        if(source == removeDispatcher) {
            if(!dispatchers.isEmpty()) {
                dispatchers.get(dispatchers.size()-1).isAlive = false;
                dispatchers.remove(dispatchers.size()-1);
                drawPanel.repaint();
            }
            dispatchMachineAmt.setText("Dispatchers: "+dispatchers.size()+"    Machines: "+machines.size());
        }
        if(source == addMachine) {
            if(machines.size() < MAX_COMPONENTS) {
                Machine newMachine = new Machine(conveyorBelts);
                machines.add(newMachine);
                Thread machineThread = new Thread(newMachine);
                newMachine.isAlive = true;
                machineThread.start();
            }
            dispatchMachineAmt.setText("Dispatchers: "+dispatchers.size()+"    Machines: "+machines.size());
        }
        if(source == removeMachine) {
            if(!machines.isEmpty()) {
                machines.get(machines.size()-1).isAlive = false;
                machines.remove(machines.size()-1);
                drawPanel.repaint();
            }
            dispatchMachineAmt.setText("Dispatchers: "+dispatchers.size()+"    Machines: "+machines.size());
        }
        drawPanel.repaint();
    }
    
    class SliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider)e.getSource();
            
            if(!source.getValueIsAdjusting()) {
                if(source == consumptionTime) {
                    Machine.MAX_CONSUMPTION_TIME = source.getValue();
                }
                if(source == productionTime) {
                    Machine.MAX_PRODUCTION_TIME = source.getValue();
                }
                drawPanel.repaint();
            }
        }
    }
    
    class DrawPanel extends JPanel {
        Random r;
        
        public DrawPanel() {
            super();
            
            r = new Random();
            
            setPreferredSize(new Dimension(1000, 750));
            setBackground(Color.WHITE);
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            int x = 100;
            int y = 120;
            int width = 80;
            int height = 60;
            
            for(int i = 0; i < MAX_COMPONENTS; i++) {
                conveyorBelts[i].drawBelt(g, x, y, width, height);
                y+=100;
            }
        }
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Factory Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new FactorySimulatorGUI());
        frame.pack();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = frame.getSize();
        frame.setLocation((screenDimension.width-frameDimension.width)/2,
           (screenDimension.height-frameDimension.height)/2);
        frame.setVisible(true);
    }
}
