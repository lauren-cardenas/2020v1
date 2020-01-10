/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //DRIVE TRAIN DEFINITIONS

  private final WPI_VictorSPX m_leftMotor1 = new WPI_VictorSPX(Map.m_leftMotor1);
  private final WPI_VictorSPX m_leftMotor2 = new WPI_VictorSPX(Map.m_leftMotor2);
  private final WPI_VictorSPX m_rightMotor1 = new WPI_VictorSPX(Map.m_rightMotor1);
  private final WPI_VictorSPX m_rightMotor2 = new WPI_VictorSPX(Map.m_rightMotor2);

  private final SpeedControllerGroup m_leftMotors = new SpeedControllerGroup(m_leftMotor1, m_leftMotor2);
  private final SpeedControllerGroup m_rightMotors = new SpeedControllerGroup(m_rightMotor1, m_rightMotor2);

  private final DifferentialDrive m_driveTrain = new DifferentialDrive(m_leftMotors, m_rightMotors);

  private final Encoder m_leftEncoder = new Encoder(Map.m_leftEnc1,Map.m_leftEnc2);
  private final Encoder m_rightEncoder = new Encoder(Map.m_rightEnc1,Map.m_rightEnc2);

  //CONTROLLER DEFINITIONS

  private final XboxController m_driverController = new XboxController(Map.DRIVER_CONTROLLER);
  private final XboxController m_operatorController = new XboxController(Map.OPERATOR_CONTROLLER);

  private double triggerVal = 
    (m_driverController.getTriggerAxis(Hand.kRight)
    - m_driverController.getTriggerAxis(Hand.kLeft))
    * Map.DRIVING_SPEED;

  private double stick = 
    (m_driverController.getX(Hand.kLeft))
    * Map.TURNING_RATE;
  
  //GAME TIMER
  
  private final Timer m_timer = new Timer();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    //Resets the timer at the beginning of autonomous.
    m_timer.reset();

    //Starts the timer at the beginning of autonomous.
    m_timer.start();

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
    //Switch is used to switch the autonomous code to whatever was chosen
    //  on your dashboard.
    //Make sure to add the options under m_chooser in robotInit.
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        if (m_timer.get() < 2.0){
          m_driveTrain.arcadeDrive(0.5, 0); //drive forward at half-speed
        } else {
          m_driveTrain.stopMotor(); //stops motors once 2 seconds have elapsed
        }
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    
    m_driveTrain.tankDrive(triggerVal + stick, triggerVal - stick);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}