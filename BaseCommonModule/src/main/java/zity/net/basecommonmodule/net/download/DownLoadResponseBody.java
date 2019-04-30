package zity.net.basecommonmodule.net.download;

import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import zity.net.basecommonmodule.net.listener.DownLoadProgressListener;
import zity.net.basecommonmodule.utils.LogUtils;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:50
 * describe：
 */


public class DownLoadResponseBody extends ResponseBody {
    private ResponseBody responseBody;

    private DownLoadProgressListener progressListener;

    private BufferedSource bufferedSource;

    public DownLoadResponseBody(ResponseBody responseBody, DownLoadProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;

    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                //read() return the number of bytes read ,or -1 if this source is exhausted;
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (progressListener != null) {

                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    LogUtils.d("totalBytesRead" + totalBytesRead + "\n" + "responseBody.contentLength()" + responseBody.contentLength());
                }
                return bytesRead;
            }
        };

    }
}
