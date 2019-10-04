/*

Author: Dhameliya Vatsalkumar
Email: vatsal17137@gmail.com

*/

import java.util.*;
import java.io.*;

public class DM{
	public static boolean match(String a,String b){
		if(a.equals(b))
			return true;
		else
			return false;
	}
	public static boolean checkWord(String str,String Words[]){
		for(int i=0;i<Words.length;i++){
			if(match(Words[i],str))
				return true;	
		}
		return false;
	}
	public static int findIndex(String str,String weightWords[]){
		for(int i=0;i<weightWords.length;i++){
			if(match(weightWords[i],str))
				return i;	
		}
		return -1;
	}
	public static void main(String args[]) throws Exception{
		String inputFileName="input.txt";
		int maxFile = 100;
		String weightWords[]={"exciting","adorable","lovely","amazing","nice","fabulous","good","awesome","positive","like","fantastic",
							  "rocking","happily","creativity","dazzle",
							  "bad","nagative","hate","worst","not","boring","sadly","stupid"};
		int dataset[][] = new int[maxFile][weightWords.length];
		int fileIndex=-1;
		int labels[] = new int[maxFile];
		
		//int noOfTurn = 2;
		//for(int x=0;x<noOfTurn;x++){
		for(int z=0;z<2;z++){
			File folder;
			File[] listOfFiles;
			if(z==0){
				folder = new File("./pos");
				listOfFiles = folder.listFiles();
			}
			else{
				folder = new File("./nag");
				listOfFiles = folder.listFiles();
			}
			for(int j=0;j<listOfFiles.length;j++){
				if(listOfFiles[j].isFile()){
					fileIndex++;
					String fileName = listOfFiles[j].getName();
					if(z==0){
						fileName = "./pos/"+fileName;
						labels[fileIndex] = 1;
					}
					else{
						fileName = "./nag/"+fileName;
						labels[fileIndex] = 0;
					}
					String text = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
					text = text.toLowerCase();
					String textInWord[] = text.split(" |\\.|,");
					
					HashMap<String,Integer> freq = new HashMap<String,Integer>(); 
					for(int i=0;i<textInWord.length;i++){
						if(checkWord(textInWord[i],weightWords)){
							if(freq.containsKey(textInWord[i])){
								int temp = (int)freq.get(textInWord[i]);
								temp+=1;
								freq.put(textInWord[i],temp);
							}
							else
								freq.put(textInWord[i],1);
						}
					}
					
					for(Map.Entry m:freq.entrySet()){
						int ind = findIndex((String)m.getKey(),weightWords);
						dataset[fileIndex][ind] = (int)m.getValue();
					}
				}
			}	
		}
		//}
		/*for(int i=0;i<fileIndex+1;i++){
			for(int j=0;j<dataset[i].length;j++){
				System.out.print(dataset[i][j]+" ");
			}
			System.out.print(labels[i]);
			System.out.println();
		}*/
//------------------------------------------------------------------------------------------------------		
		int posCount = 0;
		int nagCount = 0;
		for(int i=0;i<fileIndex+1;i++){
			if(labels[i]==1)
				posCount++;
			else
				nagCount++;
		}
		double posProb =(double) posCount/(fileIndex+1);
		double nagProb =(double) nagCount/(fileIndex+1);
		
		int wordCount[][] = new int[2][weightWords.length];
		for(int i=0;i<2;i++)
			for(int j=0;j<wordCount.length;j++)
				wordCount[i][j]=0;

		
		for(int i=0;i<fileIndex+1;i++){
			if(labels[i]==0){
				for(int j=0;j<dataset[i].length;j++)
					wordCount[0][j] += dataset[i][j];
			}
			else{
				for(int j=0;j<dataset[i].length;j++)
					wordCount[1][j] += dataset[i][j];
			}
		}
		
		/*for(int i=0;i<wordCount.length;i++){
			for(int j=0;j<wordCount[i].length;j++)
				System.out.print(wordCount[i][j]+" ");
			System.out.println();
		}*/
//-------------------------------------------------------------------------------------------------------------	

		String inputText = new Scanner(new File(inputFileName)).useDelimiter("\\Z").next();
		inputText = inputText.toLowerCase();
		String inputSplit[] = inputText.split(" |\\.|,");	
		
		double wordPosProb = 1;
		double wordNagProb = 1;
		boolean posFlag = false;
		boolean nagFlag = false;
		
		for(int i=0;i<wordCount[0].length;i++)
			if(wordCount[0][i]!=0 && checkWord(weightWords[i],inputSplit)){
				nagFlag = true;
				wordNagProb *= wordCount[0][i]/(double)nagCount;
			}
				
				
		for(int i=0;i<wordCount[1].length;i++)
			if(wordCount[1][i]!=0 && checkWord(weightWords[i],inputSplit)){
				posFlag = true;
				wordPosProb *= wordCount[1][i]/(double)posCount;
			}
				
		if(posFlag==false)
			wordPosProb = 0.0;
		if(nagFlag == false)
			wordNagProb = 0.0;	
//-------------------------------------------------------------------------------------------------------------		
		//System.out.println(posProb+" "+wordPosProb);
		//System.out.println(nagProb+" "+wordNagProb);
		double posClassProb = posProb*wordPosProb;
		double nagClassProb = nagProb*wordNagProb;
		
		
		//System.out.println(fileIndex+1);
		//System.out.println(posClassProb);
		//System.out.println(nagClassProb);
		
		if(posClassProb < nagClassProb)
			System.out.println("Nagative");
		else
			System.out.println("Positive");
			
	}
}
