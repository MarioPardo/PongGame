void setup() 
{
pinMode(A0, INPUT); 
Serial.begin(9600);
}

void loop() {
int val = analogRead(0);
int mapped = map(val, 0, 1023, 100, 800);
Serial.println(mapped);
 

}
