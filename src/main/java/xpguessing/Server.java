package xpguessing;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.mail.Folder;
import javax.mail.search.FlagTerm;
import javax.mail.Flags;
import javax.mail.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class Server {
	
	private Map<String, String> userEmail = new HashMap<>(); //从邮箱得知名字
	private Map<String, String> userName = new HashMap<>(); //从名字得知邮箱
	private Map<String, String> xp = new HashMap<>(); //XP列表，key是邮箱，element是名字
	private ArrayList<String> pool = new ArrayList<>(); //玩家邮箱列表，用于抽取
	private String ghost = null; //鬼的邮箱
	private String death = "n"; //本轮死者
	
	Map<String, String> getUserEmail(){
		return userEmail;
	}
	
	Map<String, String> getUserName(){
		return userName;
	}
	
	Map<String, String> getXp(){
		return xp;
	}
	
	ArrayList<String> getPool(){
		return pool;
	}
	
	String getDeath() {
		return death;
	}
	
	void resetDeath(String string) {
		death = string;
	}
	
	//get and analyze email
    void server() {
		String host = "outlook.office365.com";
		String username = "Game240108@hotmail.com";
		String password = "straw123.";
		
		Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", host);
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");
        
        try {
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(host, username, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

            Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            for (Message message : messages) {
            	Address[] from = message.getFrom();
                String fromEmail = from != null ? ((InternetAddress) from[0]).getAddress() : "";
                Multipart multipart = (Multipart) message.getContent();
                String text = decode(multipart);

                if(message.getSubject().equals("注册")) {
                	userEmail.put(fromEmail, text);
                	userName.put(text, fromEmail);
                }
                else if(message.getSubject().equals("xp") 
                		| message.getSubject().equals("Xp") 
                		| message.getSubject().equals("XP")) {
                	xp.put(fromEmail, text);
                	pool.add(fromEmail);
                }
                else if(message.getSubject().equals("杀人")) {
                	if(fromEmail.equals(ghost)) {
                		death = text;
                	}
                }
                else {
                	continue;
                }
            }

            emailFolder.close(false);
            store.close();
            
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	//decode email text
	private static String decode(Multipart multipart) throws MessagingException, IOException {
	    StringBuilder result = new StringBuilder();
	    int count = multipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = multipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result.append(bodyPart.getContent().toString());
	            break;
	        }
	        else if(bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            result.append(org.jsoup.Jsoup.parse(html).text());
	        }
	        else if (bodyPart.getContent() instanceof Multipart) {
	            result.append(decode((Multipart) bodyPart.getContent()));
	        }
	    }
	    return result.toString();
	}
	
	String poll() {
		ghost = pool.get(new Random().nextInt(pool.size()));
		return xp.get(ghost);
	}
	
	boolean check(String name) {
		String email = userName.get(name);
		if(email.equals(ghost)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	void Reset() {
		xp.clear();
		pool.clear();
	}
}
