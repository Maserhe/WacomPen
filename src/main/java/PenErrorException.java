/**
 * 描述:
 * 报错提示信息
 *
 * @author Maserhe
 * @create 2021-09-29 20:59
 */
public class PenErrorException extends Exception{

    public PenErrorException() {
    }

    public PenErrorException(String message) {
        super(message);
    }

    public PenErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public PenErrorException(Throwable cause) {
        super(cause);
    }
}
