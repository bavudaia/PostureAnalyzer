# Usage of AT&T M2X Apis and dashboard

Device : https://m2x.att.com/devices/79deabe7c1a7c3df4f40a1400c03db65

#Streams for each sensor is sent to the above device id and the stream is controlled as follows
Streams : p1,p2,p3,p4,d,angle,isharmful

#DashBoard: A dashboard that displays user posture analysis data where user can check progress realtime.
https://m2x.att.com/dashboards/870bf6ea


#Usage of IBM BlueMix

Used node red to send alert message when the chair user sits in an un-natural posture.
Node-Red json is in final.json file


#Working Principle:

4 Force sensitive resistors to sense 4 pressure points on the chair, a distance sensor to check the user distance from the
backrest. 
MQTT broker in EC2 instance
RealTime data is sent to IBM Bluemix MQTT subscriber to sent Twilio alert
RealTime data is sent using M2X api and device streams to the fill a M2X dashboard.
