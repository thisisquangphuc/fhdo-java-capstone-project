/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Van Dao
 */
public class Consumption {
    public int consumtion = 0;
    private int currentConsum;
    public void Consumtion(){
        for(int i=0; i<10; i++) {
            consumtion++;
            setConsumtion(consumtion);
            
        }
    }
	
	
    public void setConsumtion(int a) {
        currentConsum = a;
    }
	
    public int getConsumtion() {
	return currentConsum;
    }
}
