import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TimerTask;
import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.MoveProvider;
import lejos.utility.Delay;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class DPVedKor {

	//EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);
	//SampleProvider sp = gyroSensor.getAngleMode();


	public static void main(String[] args) {
		while(true) {
			try {
				new DPVedKor().run();
			}
			catch(Exception e) {

			}
		}

	}

	public void run() throws Exception {

		Button.ESCAPE.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(Key k) {
				System.exit(0);
			}

			@Override
			public void keyPressed(Key k) {
				System.exit(0);
			}
		});

		String clientSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);

		while (true) {
			System.out.println("afventer");
			final Socket ConnectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(ConnectionSocket.getInputStream()));

			clientSentence = inFromClient.readLine();





			System.out.println(clientSentence);

			if(clientSentence.equals("exit")) {
				break;
			} 
			else if(clientSentence.equals("w")) { //fremad
				Motor.A.setSpeed(100);
				Motor.B.setSpeed(100);
				Motor.A.backward();
				Motor.B.backward();
				System.out.println("w");
			} 
			else if(clientSentence.equals("sound")) { //fremad


				Sound.setVolume(99);
				Sound.buzz();


			} 
			else if(clientSentence.equals("ww")) { //fremad
				Motor.A.setSpeed(500);
				Motor.B.setSpeed(500);
				Motor.A.backward();
				Motor.B.backward();
				System.out.println("ww");
			} 
			else if(clientSentence.equals("www")) { //hurtigt fremad
				Motor.A.setSpeed(1000);
				Motor.B.setSpeed(1000);
				Motor.A.backward();
				Motor.B.backward();


			} 
			else if(clientSentence.equals("e")) { //suger bolde
				try {
					Motor.C.setSpeed(1000);
					Motor.C.backward();
					Motor.D.setSpeed(1000);
					Motor.D.forward();
				}
				catch(Exception e) {

				}
				finally {
					DataOutputStream out = new DataOutputStream(ConnectionSocket.getOutputStream());
					out.writeBytes("ok\n");
					out.flush();
					out.close();
				}
			}
			else if(clientSentence.equals("r")) { //smider bolde ud
				try {
					Motor.C.setSpeed(300);
					Motor.C.forward();
					Motor.D.setSpeed(300);
					Motor.D.backward();
				}
				catch(Exception e) {

				}
				finally {
					DataOutputStream out = new DataOutputStream(ConnectionSocket.getOutputStream());
					out.writeBytes("ok\n");
					out.flush();
					out.close();

				}

			}
			else if(clientSentence.equals("rr")) { //smider bolde ud
				try {
					Motor.C.setSpeed(1000);
					Motor.C.forward();
					Motor.D.setSpeed(1000);
					Motor.D.backward();
				}
				catch(Exception e) {

				}
				finally {
					DataOutputStream out = new DataOutputStream(ConnectionSocket.getOutputStream());
					out.writeBytes("ok\n");
					out.flush();
					out.close();

				}
			}
			else if(clientSentence.equals("t")) { //stop suge
				Motor.C.stop(true);
				Motor.D.stop(true);
			}
			else if (clientSentence.equals("s")) { //kør tilbage
				Motor.A.setSpeed(250);
				Motor.B.setSpeed(250);
				Motor.A.forward();
				Motor.B.forward();
			} 
			else if (clientSentence.equals("q")) { //kør-stop
				Motor.A.stop(true);
				Motor.B.stop(true);
			} 
			else if(clientSentence.equals("a")) { //drej til venstre
				Motor.A.setSpeed(100);
				Motor.B.setSpeed(100);
				Motor.A.backward();
				Motor.B.forward();
			}
			else if(clientSentence.equals("aa")) { //drej til venstre hurtigt
				Motor.A.setSpeed(500);
				Motor.B.setSpeed(500);
				Motor.A.backward();
				Motor.B.forward();
			}
			else if(clientSentence.equals("aaa")) { //drej til venstre hurtigt
				Motor.A.setSpeed(1000);
				Motor.B.setSpeed(1000);
				Motor.A.backward();
				Motor.B.forward();
			}
			else if(clientSentence.equals("d")) { //drej til højre
				Motor.A.setSpeed(100);
				Motor.B.setSpeed(100);
				Motor.A.forward();
				Motor.B.backward();
			}
			else if(clientSentence.equals("dd")) { //drej til højre hurtigt
				Motor.A.setSpeed(500);
				Motor.B.setSpeed(500);
				Motor.A.forward();
				Motor.B.backward();
			}
			else if(clientSentence.equals("ddd")) { //drej til højre hurtigt
				Motor.A.setSpeed(1000);
				Motor.B.setSpeed(1000);
				Motor.A.forward();
				Motor.B.backward();
			}

			else if(clientSentence.startsWith("fremkor")) { //kør fremad antal cm
				try {
					clientSentence=clientSentence.replaceAll("fremkor", "");
					clientSentence=clientSentence.trim();
					final float afstandFloat = Float.parseFloat(clientSentence);

					DifferentialPilot pilot = new DifferentialPilot(5.45, 11 ,Motor.B, Motor.A);
					pilot.setLinearSpeed(1000);
					pilot.travel(-afstandFloat, true); //TODO: hvorfor true?				
					pilot.addMoveListener(new MoveListener() {
						@Override
						public void moveStopped(Move event, MoveProvider mp) {
							DataOutputStream out = null;
							try {
								out = new DataOutputStream(ConnectionSocket.getOutputStream());
								out.writeBytes("ok\n");
								out.flush();
								out.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								 e.printStackTrace();
								return;
							}


						}
						
						

						@Override
						public void moveStarted(Move event, MoveProvider mp) {
							// TODO Auto-generated method stub

						}
					});


				}
				catch(Exception e) {
					return;

				}
				finally {

				}

			}
			else if(clientSentence.startsWith("hojrebak")) { //kør fremad antal cm

				try {

					System.out.println("motor a: "+Motor.A.getMaxSpeed());
					System.out.println("motor a: "+Motor.B.getMaxSpeed());

					clientSentence=clientSentence.replaceAll("hojrebak", "");
					clientSentence=clientSentence.trim();
					System.out.println(clientSentence);
					//final int afstand = Integer.parseInt(clientSentence);
					final float afstandFloat = Float.parseFloat(clientSentence);


					Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 8).offset(5.55);
					Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 5.5).offset(-5.55);
					Chassis myChassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
					MovePilot pilot = new MovePilot(myChassis);
					//pilot.setAngularSpeed(400);
					// pilot.setAngularAcceleration(200);
					pilot.setLinearSpeed(200);
					pilot.setLinearAcceleration(15);
					pilot.travel(afstandFloat, true);
					pilot.addMoveListener(new MoveListener() {

						@Override
						public void moveStopped(Move event, MoveProvider mp) {
							// TODO Auto-generated method stub
							DataOutputStream out;
							try {
								out = new DataOutputStream(ConnectionSocket.getOutputStream());
								out.writeBytes("ok\n");
								out.flush();
								out.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;

							}

						}

						@Override
						public void moveStarted(Move event, MoveProvider mp) {
							// TODO Auto-generated method stub

						}
					});
				}
				catch(Exception e) {
					return;

				}
				finally {

				}

			}
			else if(clientSentence.startsWith("venstrebak")) { //kør fremad antal cm

				try {

					System.out.println("motor a: "+Motor.A.getMaxSpeed());
					System.out.println("motor a: "+Motor.B.getMaxSpeed());

					clientSentence=clientSentence.replaceAll("venstrebak", "");
					clientSentence=clientSentence.trim();
					System.out.println(clientSentence);
					//final int afstand = Integer.parseInt(clientSentence);
					final float afstandFloat = Float.parseFloat(clientSentence);


					Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 5.5).offset(5.55);
					Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 8).offset(-5.55);
					Chassis myChassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
					MovePilot pilot = new MovePilot(myChassis);
					//pilot.setAngularSpeed(400);
					// pilot.setAngularAcceleration(200);
					pilot.setLinearSpeed(200);
					pilot.setLinearAcceleration(15);
					pilot.travel(afstandFloat, true);
					pilot.addMoveListener(new MoveListener() {

						@Override
						public void moveStopped(Move event, MoveProvider mp) {
							// TODO Auto-generated method stub
							DataOutputStream out;
							try {
								out = new DataOutputStream(ConnectionSocket.getOutputStream());
								out.writeBytes("ok\n");
								out.flush();
								out.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;

							}

						}

						@Override
						public void moveStarted(Move event, MoveProvider mp) {
							// TODO Auto-generated method stub

						}
					});
				}
				catch(Exception e) {
					return;

				}
				finally {

				}

			}
			else if(clientSentence.startsWith("langfremkor")) { //kør fremad antal cm

				try {

					System.out.println("motor a: "+Motor.A.getMaxSpeed());
					System.out.println("motor a: "+Motor.B.getMaxSpeed());

					clientSentence=clientSentence.replaceAll("langfremkor", "");
					clientSentence=clientSentence.trim();
					System.out.println(clientSentence);

					//final int afstand = Integer.parseInt(clientSentence);
					//final double afstandDouble = Double.parseDouble(clientSentence);
					final float afstandFloat = Float.parseFloat(clientSentence);


					Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 5.5).offset(5.55);
					Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 5.5).offset(-5.55);
					Chassis myChassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
					MovePilot pilot = new MovePilot(myChassis);
					//pilot.setAngularSpeed(400);
					// pilot.setAngularAcceleration(200);
					pilot.setLinearSpeed(6);
					//pilot.setLinearAcceleration(20);
					pilot.travel(-afstandFloat,true);
					pilot.addMoveListener(new MoveListener() {

						@Override
						public void moveStopped(Move event, MoveProvider mp) {
							DataOutputStream out;
							try {
								out = new DataOutputStream(ConnectionSocket.getOutputStream());
								out.writeBytes("ok\n");
								out.flush();
								out.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;

							}


						}

						@Override
						public void moveStarted(Move event, MoveProvider mp) {
							// TODO Auto-generated method stub

						}
					});
				}
				catch(Exception e) {
					return;

				}
				finally {

				}

			}

			else if(clientSentence.startsWith("tilbagekor")) { //kør fremad antal cm

				try {
					System.out.println("motor a: "+Motor.A.getMaxSpeed());
					System.out.println("motor a: "+Motor.B.getMaxSpeed());

					clientSentence=clientSentence.replaceAll("tilbagekor", "");
					clientSentence=clientSentence.trim();
					System.out.println(clientSentence);
					//final int afstand = Integer.parseInt(clientSentence);
					final float afstandFloat = Float.parseFloat(clientSentence);


					Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 5.5).offset(5.55);
					Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 5.5).offset(-5.55);
					Chassis myChassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
					MovePilot pilot = new MovePilot(myChassis);
					//pilot.setAngularSpeed(400);
					// pilot.setAngularAcceleration(200);
					pilot.setLinearSpeed(500);
					pilot.setLinearAcceleration(20);
					pilot.travel(afstandFloat,true);
					pilot.addMoveListener(new MoveListener() {

						@Override
						public void moveStopped(Move event, MoveProvider mp) {
							DataOutputStream out;
							try {
								out = new DataOutputStream(ConnectionSocket.getOutputStream());
								out.writeBytes("ok\n");
								out.flush();
								out.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;

							}

						}

						@Override
						public void moveStarted(Move event, MoveProvider mp) {
							// TODO Auto-generated method stub

						}
					});
				}
				catch(Exception e) {
					return;

				}
				finally {

				}

			}

			else if(clientSentence.startsWith("drej")) { //kør fremad antal cm

				try {
					System.out.println("drej trykket");


					clientSentence=clientSentence.replaceAll("drej", "");
					clientSentence=clientSentence.trim();
					System.out.println(clientSentence);
					//final int vinkel = Integer.parseInt(clientSentence);
					final float vinkelFloat = Float.parseFloat(clientSentence);


					Wheel leftWheel = WheeledChassis.modelWheel(Motor.A,5.5).offset(5.6);
					Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 5.5).offset(-5.6);
					Chassis myChassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
					MovePilot pilot = new MovePilot(myChassis);
					pilot.setAngularSpeed(200);
					//pilot.setAngularAcceleration(10);
					//pilot.setLinearSpeed(500);
					//pilot.setLinearAcceleration(200);
					pilot.rotate(vinkelFloat);
					pilot.addMoveListener(new MoveListener() {

						@Override
						public void moveStopped(Move event, MoveProvider mp) {
							DataOutputStream out;
							try {
								out = new DataOutputStream(ConnectionSocket.getOutputStream());
								out.writeBytes("ok\n");
								out.flush();
								out.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;

							}

						}

						@Override
						public void moveStarted(Move event, MoveProvider mp) {
							// TODO Auto-generated method stub

						}
					});
				}
				catch(Exception e) {
					return;

				}
				finally {

					DataOutputStream out = new DataOutputStream(ConnectionSocket.getOutputStream());
					out.writeBytes("ok\n");
					out.flush();
					out.close();

				}
			}
			else if(clientSentence.startsWith("korsdrej")) { //kør fremad antal cm

				try {
					System.out.println("korsdrej trykket");


					clientSentence=clientSentence.replaceAll("korsdrej", "");
					clientSentence=clientSentence.trim();
					System.out.println(clientSentence);
					//final int vinkel = Integer.parseInt(clientSentence);
					final float vinkelFloat = Float.parseFloat(clientSentence);


					Wheel leftWheel = WheeledChassis.modelWheel(Motor.A,5.5).offset(5.6);
					Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 5.5).offset(-5.6);
					Chassis myChassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
					MovePilot pilot = new MovePilot(myChassis);
					pilot.setAngularSpeed(80);
					//pilot.setAngularAcceleration(10);
					//pilot.setLinearSpeed(500);
					//pilot.setLinearAcceleration(200);
					pilot.rotate(vinkelFloat);
					pilot.addMoveListener(new MoveListener() {

						@Override
						public void moveStopped(Move event, MoveProvider mp) {
							DataOutputStream out;
							try {
								out = new DataOutputStream(ConnectionSocket.getOutputStream());
								out.writeBytes("ok\n");
								out.flush();
								out.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;

							}

						}

						@Override
						public void moveStarted(Move event, MoveProvider mp) {
							// TODO Auto-generated method stub

						}
					});
				}
				catch(Exception e) {
					return;

				}
				finally {

					DataOutputStream out = new DataOutputStream(ConnectionSocket.getOutputStream());
					out.writeBytes("ok\n");
					out.flush();
					out.close();

				}
			}
			
			else {
				DataOutputStream out = new DataOutputStream(ConnectionSocket.getOutputStream());
				out.writeBytes("forkert kommando\n");
				out.flush();
				out.close();
			}

		}
		welcomeSocket.close();
	}
}