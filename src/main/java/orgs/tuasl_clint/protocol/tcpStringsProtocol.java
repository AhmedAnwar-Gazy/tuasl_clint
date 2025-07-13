package orgs.tuasl_clint.protocol;

import com.gluonhq.richtextarea.model.TextBuffer;
import com.mysql.cj.x.protobuf.MysqlxCrud;

import java.io.File;

public class tcpStringsProtocol {
    public  interface OnSentListiner {
        public void onSent(File file);
    }
    public interface OnRecivedListiner{
        public void onRecived(File file);
    }
    public interface WhileSendingListiner{
        public void whileSending(long packetSent, long packetsCount, byte[] packets);
    }
    public interface WileRecivingListiner{
        public void whileReciving(long packetSent, long packetsCount, byte[] packets);
    }
    public interface OnFailListiner{
        public void onFail(long  packetSent,long packetsCount, byte[] packets);
    }

    public static class Packet{
        private long packetID;
        private byte[] data;

        public Packet(long packetID, byte[] data) {
            this.packetID = packetID;
            this.data = data;
        }

        public long getPacketID() {
            return packetID;
        }

        public byte[] getData() {
            return data;
        }

        public void setPacketID(long packetID) {
            this.packetID = packetID;
        }

        public void setData(byte[] data) {
            this.data = data;
        }
    }

    private OnRecivedListiner onRecivedListiner;
    private OnSentListiner onSentListiner;
    private WhileSendingListiner whileSendingListiner;
    private WileRecivingListiner wileRecivingListiner;
    private OnFailListiner onFailListiner;

    private File file;
    private long PORT;
    private String SERVER_IP;


    public void setOnSentListiner(OnSentListiner onSentListiner) {
        this.onSentListiner = onSentListiner;
    }

    public void setOnRecivedListiner(OnRecivedListiner onRecivedListiner) {
        this.onRecivedListiner = onRecivedListiner;
    }

    public void setWhileSendingListiner(WhileSendingListiner whileSendingListiner) {
        this.whileSendingListiner = whileSendingListiner;
    }

    public void setWileRecivingListiner(WileRecivingListiner wileRecivingListiner) {
        this.wileRecivingListiner = wileRecivingListiner;
    }

    public void setOnFailListiner(OnFailListiner onFailListiner) {
        this.onFailListiner = onFailListiner;
    }

    public void removeOnSentListiner(OnSentListiner onSentListiner) {
        this.onSentListiner = null;
    }

    public void removeOnRecivedListiner(OnRecivedListiner onRecivedListiner) {
        this.onRecivedListiner = null;
    }

    public void removeWhileSendingListiner(WhileSendingListiner whileSendingListiner) {
        this.whileSendingListiner = null;
    }

    public void removeWileRecivingListiner(WileRecivingListiner wileRecivingListiner) {
        this.wileRecivingListiner = null;
    }

    public void removeOnFailListiner(OnFailListiner onFailListiner) {
        this.onFailListiner = null;
    }



}
