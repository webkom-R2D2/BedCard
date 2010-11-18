package no.abakus.bedcard.storage.filemanager;

import java.awt.Image;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;
import no.abakus.naut.entity.news.Type;

import org.apache.log4j.Logger;
 
public class BedCardFileManager implements FileManager {
	private static Logger log = Logger.getLogger(BedCardFileManager.class);
	private ArrayList<Type> typer;
	
	public BedCardFileManager() {
		super();
		finnTyper();
	}

	@Override
	public BedCardSaveState loadFromFile(Long id, Type type) throws FileManagerException {
		String folderPath = new java.io.File("").getAbsolutePath()+"\\BedCardSavedEvents\\";
		String folderName = type.getId()+"";
		try {
	        XMLDecoder decoder = new XMLDecoder(
	        		new BufferedInputStream(new FileInputStream(folderPath+folderName+"\\"+id+".xml")));
	        Object object  = decoder.readObject();
	 
	        decoder.close();
	        
	        return (BedCardSaveState)object;
	        
		} catch (Exception e){ 
			log.error("loadFromFile(): Noe gikk galt under lasting");
			throw new FileManagerException("Noe gikk galt under lasting");
		}
	}

	@Override
	public boolean saveToFile(BedCardSaveState save) throws FileManagerException{
		log.debug("Saving to file");
		String folderPath = new java.io.File("").getAbsolutePath()+"\\BedCardSavedEvents\\";
		String folderName = save.getEvent().getType().getId()+"";
		String filename = save.getEvent().getId()+".xml";
		
		//Bildet funker ikke:
		Image img = save.getEvent().getImage();
		save.getEvent().setImage(null);
		
		if (!exists(filename, new File(folderPath+folderName))) {
			File f1 = new File(folderPath);
			File f2 = new File(folderPath + folderName);
			f1.mkdir();
			f2.mkdir();
		}
		try {
			
			XMLEncoder encoder = new XMLEncoder(
	            new BufferedOutputStream(new FileOutputStream(folderPath+folderName+"\\"+filename)));

			encoder.writeObject(save);
			encoder.close();
			sjekkType(save.getEvent().getType());
			save.getEvent().setImage(img);
			return true;
		}  catch (Exception e){
			save.getEvent().setImage(img);
			throw new FileManagerException("Noe gikk galt under lagring");
		}
	}
	
	@Override
	public ArrayList<BedCardSaveState> getListOfSavedEvents(Type type) throws FileManagerException {
		if(type == null)
			return new ArrayList<BedCardSaveState>();
		String folderPath = new java.io.File("").getAbsolutePath()+"\\BedCardSavedEvents\\";
		String folderName = type.getId()+"";
		String folder = folderPath+folderName;
		
		ArrayList<BedCardSaveState> listOfSavedEvents = new ArrayList<BedCardSaveState>();
		try {
			File dir = new File(folder);			
			String[] fileList = dir.list();
			
			XMLDecoder decoder;
			for (int i = 0; i < fileList.length; i++){
				decoder = new XMLDecoder(
		        		new BufferedInputStream(new FileInputStream(folder+"\\"+fileList[i])));
		        Object object  = decoder.readObject();
		        
		        listOfSavedEvents.add((BedCardSaveState)object);
		        
		        decoder.close();
			}
		}  catch (Exception e){ 
			log.error("getListOfSavedEvents(): Noe gikk galt under lasting");
			removeType(type);
			throw new FileManagerException("Noe gikk galt under lasting");
		}
		return listOfSavedEvents;     
	}
 
    private boolean exists (String filename, File dir) {
        boolean exists = false;
 
        if (new File (dir, filename).exists ()) {
            exists = true;
        } else {
            File[] subdirs = dir.listFiles ();
 
            int i = 0;
            int n = (subdirs == null) ? 0 : subdirs.length;
 
            while ((i < n) && ! exists) {
                File subdir = subdirs[i];
 
                if (subdir.isDirectory ()) {
                    exists = exists (filename, subdir);
                }
 
                i ++;
            }
        }
 
        return exists;
    }

	@Override
	public ArrayList<Type> getAllTypes() throws FileManagerException {
		return typer;
	}
	private void finnTyper(){
		String folderPath = new java.io.File("").getAbsolutePath()+"\\BedCardSavedEvents\\";
        try {
			XMLDecoder decoder = new XMLDecoder(
					new BufferedInputStream(new FileInputStream(folderPath+"typer.xml")));
				Object object  = decoder.readObject();
 
				decoder.close();
				
				typer = (ArrayList<Type>)object;
			} catch (FileNotFoundException e) {
				log.info("finnTyper(): Noe gikk galt under lasting, har ingen typer fil");
			typer = new ArrayList<Type>();
		}
	}
	private void removeType(Type type){
		for(int i = 0; i<typer.size(); i++){
			if(typer.get(i).getId() == type.getId()){
				typer.remove(i);
				break;
			}
		}
		String folderPath = new java.io.File("").getAbsolutePath()+"\\BedCardSavedEvents\\";
		String filename = "typer.xml";

		if (!exists(filename, new File(folderPath))) {
			File f1 = new File(folderPath);
			f1.mkdir();
		}
	
		try {
			
			XMLEncoder encoder = new XMLEncoder(
	            new BufferedOutputStream(new FileOutputStream(folderPath+filename)));
			encoder.writeObject(typer);
			encoder.close();
		}  catch (Exception e){ 
			log.error("Noe gikk galt under lagring av typer");
		}				
	}
	
	private void sjekkType(Type type){
		boolean found = false;
		for(Type t : typer)
			if(t.getId().intValue() == type.getId().intValue())
				found = true;
		if(!found){
			typer.add(type);
			String folderPath = new java.io.File("").getAbsolutePath()+"\\BedCardSavedEvents\\";
			String filename = "typer.xml";

			if (!exists(filename, new File(folderPath))) {
				File f1 = new File(folderPath);
				f1.mkdir();
			}
		
			try {
				XMLEncoder encoder = new XMLEncoder(
		            new BufferedOutputStream(new FileOutputStream(folderPath+filename)));
				encoder.writeObject(typer);
				encoder.close();
			}  catch (Exception e){ 
				log.error("Noe gikk galt under lagring av typer");
			}
		}
	}
}
