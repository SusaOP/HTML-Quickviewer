package htmlquickview;

import java.util.zip.CRC32;

// A class to aid in hashing to generate a unique html filename
public class Hashing {

    private CRC32 crc32;
    public Hashing(){
        this.crc32 = new CRC32();
    }//hash a string
    public long hashString(String strToHash){
        this.crc32.update(strToHash.getBytes());
        return this.crc32.getValue();
    }
}
