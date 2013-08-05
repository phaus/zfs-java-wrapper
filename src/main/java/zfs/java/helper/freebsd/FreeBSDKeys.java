/**
 * FreeBSDKeys 05.08.2013
 *
 * @author Philipp Haussleiter
 *
 */
package zfs.java.helper.freebsd;

public final class FreeBSDKeys {

    public static final String DEVICE_DESCRIPTION_MARKER = "<";
    public static final String DEVICE_BUS_MARKER = "at";
    public static final String DEVICE_TRANSFER_MARKER = "MB/s ";
    public static final String IDE_CDROM = "acd";
    public static final String DEVICE_SIZE_MARKER = "byte sectors:";
    /*
     thx to http://www.cyberciti.biz/faq/freebsd-hard-disk-information
     a] IDE Hard disk names starts with ad - /dev/ad0 first IDE hard disk, /dev/ad1 second hard disk and so on
     b] SCSI Hard disk names starts with da - /dev/da*
     c] IDE CDROM/RW/DVD names starts with acd - /dev/acd*
     d] SCSI CDROM/RW/DVD names starts with cd - /dev/cd*
     */
    public static final String IDE_HDD = "ad";
    public static final String GPTID_HEADER = "Name  Status  Components";
    public static final int DEVICE_SIZE = 2;
    public static final String SCSI_CDROM = "cd";
    public static final int DEVICE_BUS = 1;
    public static final String SCSI_HDD = "da";
    public static final String DEVICE_SIZE_DEVIDER = "MB ";
    public static final int DEVICE_TRANSFER = 0;
    public static final int DEVICE_DESCRIPTION = 3;

    private FreeBSDKeys() {
    }
}
