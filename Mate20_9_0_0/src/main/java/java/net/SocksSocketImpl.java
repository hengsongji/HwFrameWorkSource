package java.net;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.sql.Types;
import sun.security.action.GetPropertyAction;

class SocksSocketImpl extends PlainSocketImpl implements SocksConsts {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private boolean applicationSetProxy;
    private InputStream cmdIn;
    private OutputStream cmdOut;
    private Socket cmdsock;
    private InetSocketAddress external_address;
    private String server;
    private int serverPort;
    private boolean useV4;

    SocksSocketImpl() {
        this.server = null;
        this.serverPort = SocksConsts.DEFAULT_PORT;
        this.useV4 = false;
        this.cmdsock = null;
        this.cmdIn = null;
        this.cmdOut = null;
    }

    SocksSocketImpl(String server, int port) {
        this.server = null;
        int i = SocksConsts.DEFAULT_PORT;
        this.serverPort = SocksConsts.DEFAULT_PORT;
        this.useV4 = false;
        this.cmdsock = null;
        this.cmdIn = null;
        this.cmdOut = null;
        this.server = server;
        if (port != -1) {
            i = port;
        }
        this.serverPort = i;
    }

    SocksSocketImpl(Proxy proxy) {
        this.server = null;
        this.serverPort = SocksConsts.DEFAULT_PORT;
        this.useV4 = false;
        this.cmdsock = null;
        this.cmdIn = null;
        this.cmdOut = null;
        SocketAddress a = proxy.address();
        if (a instanceof InetSocketAddress) {
            InetSocketAddress ad = (InetSocketAddress) a;
            this.server = ad.getHostString();
            this.serverPort = ad.getPort();
        }
    }

    void setV4() {
        this.useV4 = true;
    }

    private synchronized void privilegedConnect(final String host, final int port, final int timeout) throws IOException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                public Void run() throws IOException {
                    SocksSocketImpl.this.superConnectServer(host, port, timeout);
                    SocksSocketImpl.this.cmdIn = SocksSocketImpl.this.getInputStream();
                    SocksSocketImpl.this.cmdOut = SocksSocketImpl.this.getOutputStream();
                    return null;
                }
            });
        } catch (PrivilegedActionException pae) {
            throw ((IOException) pae.getException());
        }
    }

    private void superConnectServer(String host, int port, int timeout) throws IOException {
        super.connect((SocketAddress) new InetSocketAddress(host, port), timeout);
    }

    private static int remainingMillis(long deadlineMillis) throws IOException {
        if (deadlineMillis == 0) {
            return 0;
        }
        long remaining = deadlineMillis - System.currentTimeMillis();
        if (remaining > 0) {
            return (int) remaining;
        }
        throw new SocketTimeoutException();
    }

    private int readSocksReply(InputStream in, byte[] data) throws IOException {
        return readSocksReply(in, data, 0);
    }

    private int readSocksReply(InputStream in, byte[] data, long deadlineMillis) throws IOException {
        int len = data.length;
        int received = 0;
        int attempts = 0;
        while (received < len && attempts < 3) {
            try {
                int count = ((SocketInputStream) in).read(data, received, len - received, remainingMillis(deadlineMillis));
                if (count >= 0) {
                    received += count;
                    attempts++;
                } else {
                    throw new SocketException("Malformed reply from SOCKS server");
                }
            } catch (SocketTimeoutException e) {
                throw new SocketTimeoutException("Connect timed out");
            }
        }
        return received;
    }

    private boolean authenticate(byte method, InputStream in, BufferedOutputStream out) throws IOException {
        return authenticate(method, in, out, 0);
    }

    private boolean authenticate(byte method, InputStream in, BufferedOutputStream out, long deadlineMillis) throws IOException {
        if (method == (byte) 0) {
            return true;
        }
        if (method != (byte) 2) {
            return false;
        }
        String userName;
        String password = null;
        final InetAddress addr = InetAddress.getByName(this.server);
        PasswordAuthentication pw = (PasswordAuthentication) AccessController.doPrivileged(new PrivilegedAction<PasswordAuthentication>() {
            public PasswordAuthentication run() {
                return Authenticator.requestPasswordAuthentication(SocksSocketImpl.this.server, addr, SocksSocketImpl.this.serverPort, "SOCKS5", "SOCKS authentication", null);
            }
        });
        if (pw != null) {
            userName = pw.getUserName();
            password = new String(pw.getPassword());
        } else {
            userName = (String) AccessController.doPrivileged(new GetPropertyAction("user.name"));
        }
        if (userName == null) {
            return false;
        }
        out.write(1);
        out.write(userName.length());
        try {
            out.write(userName.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
        }
        if (password != null) {
            out.write(password.length());
            try {
                out.write(password.getBytes("ISO-8859-1"));
            } catch (UnsupportedEncodingException e2) {
            }
        } else {
            out.write(0);
        }
        out.flush();
        byte[] data = new byte[2];
        if (readSocksReply(in, data, deadlineMillis) == 2 && data[1] == (byte) 0) {
            return true;
        }
        out.close();
        in.close();
        return false;
    }

    private void connectV4(InputStream in, OutputStream out, InetSocketAddress endpoint, long deadlineMillis) throws IOException {
        if (endpoint.getAddress() instanceof Inet4Address) {
            out.write(4);
            out.write(1);
            out.write((endpoint.getPort() >> 8) & 255);
            out.write((endpoint.getPort() >> 0) & 255);
            out.write(endpoint.getAddress().getAddress());
            try {
                out.write(getUserName().getBytes("ISO-8859-1"));
            } catch (UnsupportedEncodingException e) {
            }
            out.write(0);
            out.flush();
            byte[] data = new byte[8];
            int n = readSocksReply(in, data, deadlineMillis);
            if (n != 8) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Reply from SOCKS server has bad length: ");
                stringBuilder.append(n);
                throw new SocketException(stringBuilder.toString());
            } else if (data[0] == (byte) 0 || data[0] == (byte) 4) {
                SocketException ex = null;
                switch (data[1]) {
                    case (byte) 90:
                        this.external_address = endpoint;
                        break;
                    case Types.DATE /*91*/:
                        ex = new SocketException("SOCKS request rejected");
                        break;
                    case Types.TIME /*92*/:
                        ex = new SocketException("SOCKS server couldn't reach destination");
                        break;
                    case Types.TIMESTAMP /*93*/:
                        ex = new SocketException("SOCKS authentication failed");
                        break;
                    default:
                        ex = new SocketException("Reply from SOCKS server contains bad status");
                        break;
                }
                if (ex != null) {
                    in.close();
                    out.close();
                    throw ex;
                }
                return;
            } else {
                throw new SocketException("Reply from SOCKS server has bad version");
            }
        }
        throw new SocketException("SOCKS V4 requires IPv4 only addresses");
    }

    protected void connect(SocketAddress endpoint, int timeout) throws IOException {
        long deadlineMillis;
        SocketAddress socketAddress = endpoint;
        int i = timeout;
        if (i == 0) {
            deadlineMillis = 0;
        } else {
            deadlineMillis = System.currentTimeMillis() + ((long) i);
            deadlineMillis = deadlineMillis < 0 ? Long.MAX_VALUE : deadlineMillis;
        }
        long deadlineMillis2 = deadlineMillis;
        SecurityManager security = System.getSecurityManager();
        if (socketAddress == null || !(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        InetSocketAddress epoint = (InetSocketAddress) socketAddress;
        if (security != null) {
            if (epoint.isUnresolved()) {
                security.checkConnect(epoint.getHostName(), epoint.getPort());
            } else {
                security.checkConnect(epoint.getAddress().getHostAddress(), epoint.getPort());
            }
        }
        if (this.server == null) {
            super.connect((SocketAddress) epoint, remainingMillis(deadlineMillis2));
            return;
        }
        try {
            privilegedConnect(this.server, this.serverPort, remainingMillis(deadlineMillis2));
            OutputStream out = new BufferedOutputStream(this.cmdOut, 512);
            InputStream in = this.cmdIn;
            if (!this.useV4) {
                out.write(5);
                out.write(2);
                out.write(0);
                out.write(2);
                out.flush();
                byte[] data = new byte[2];
                int i2 = readSocksReply(in, data, deadlineMillis2);
                byte[] bArr;
                if (i2 != 2) {
                    bArr = data;
                } else if (data[0] != (byte) 5) {
                    int i3 = i2;
                    bArr = data;
                } else if (data[1] != (byte) -1) {
                    byte b = (byte) 1;
                    if (authenticate(data[1], in, out, deadlineMillis2)) {
                        out.write(5);
                        out.write(b);
                        out.write(0);
                        if (epoint.isUnresolved()) {
                            out.write(3);
                            out.write(epoint.getHostName().length());
                            try {
                                out.write(epoint.getHostName().getBytes("ISO-8859-1"));
                            } catch (UnsupportedEncodingException e) {
                            }
                            out.write((epoint.getPort() >> 8) & 255);
                            out.write((epoint.getPort() >> 0) & 255);
                        } else if (epoint.getAddress() instanceof Inet6Address) {
                            out.write(4);
                            out.write(epoint.getAddress().getAddress());
                            out.write((epoint.getPort() >> 8) & 255);
                            out.write((epoint.getPort() >> 0) & 255);
                        } else {
                            out.write(b);
                            out.write(epoint.getAddress().getAddress());
                            out.write((epoint.getPort() >> 8) & 255);
                            out.write((epoint.getPort() >> 0) & 255);
                        }
                        out.flush();
                        byte[] data2 = new byte[4];
                        if (readSocksReply(in, data2, deadlineMillis2) == 4) {
                            data = null;
                            switch (data2[b]) {
                                case (byte) 0:
                                    byte b2 = data2[3];
                                    if (b2 != b) {
                                        int len;
                                        switch (b2) {
                                            case (byte) 3:
                                                len = data2[b];
                                                if (readSocksReply(in, new byte[len], deadlineMillis2) != len) {
                                                    throw new SocketException("Reply from SOCKS server badly formatted");
                                                } else if (readSocksReply(in, new byte[2], deadlineMillis2) != 2) {
                                                    throw new SocketException("Reply from SOCKS server badly formatted");
                                                }
                                                break;
                                            case (byte) 4:
                                                len = data2[b];
                                                if (readSocksReply(in, new byte[len], deadlineMillis2) != len) {
                                                    throw new SocketException("Reply from SOCKS server badly formatted");
                                                } else if (readSocksReply(in, new byte[2], deadlineMillis2) != 2) {
                                                    throw new SocketException("Reply from SOCKS server badly formatted");
                                                }
                                                break;
                                            default:
                                                data = new SocketException("Reply from SOCKS server contains wrong code");
                                                break;
                                        }
                                    } else if (readSocksReply(in, new byte[4], deadlineMillis2) != 4) {
                                        throw new SocketException("Reply from SOCKS server badly formatted");
                                    } else if (readSocksReply(in, new byte[2], deadlineMillis2) != 2) {
                                        throw new SocketException("Reply from SOCKS server badly formatted");
                                    }
                                    break;
                                case (byte) 1:
                                    data = new SocketException("SOCKS server general failure");
                                    break;
                                case (byte) 2:
                                    data = new SocketException("SOCKS: Connection not allowed by ruleset");
                                    break;
                                case (byte) 3:
                                    data = new SocketException("SOCKS: Network unreachable");
                                    break;
                                case (byte) 4:
                                    data = new SocketException("SOCKS: Host unreachable");
                                    break;
                                case (byte) 5:
                                    data = new SocketException("SOCKS: Connection refused");
                                    break;
                                case (byte) 6:
                                    data = new SocketException("SOCKS: TTL expired");
                                    break;
                                case (byte) 7:
                                    data = new SocketException("SOCKS: Command not supported");
                                    break;
                                case (byte) 8:
                                    data = new SocketException("SOCKS: address type not supported");
                                    break;
                            }
                            if (data == null) {
                                this.external_address = epoint;
                                return;
                            }
                            in.close();
                            out.close();
                            throw data;
                        }
                        throw new SocketException("Reply from SOCKS server has bad length");
                    }
                    throw new SocketException("SOCKS : authentication failed");
                } else {
                    bArr = data;
                    throw new SocketException("SOCKS : No acceptable methods");
                }
                if (epoint.isUnresolved()) {
                    throw new UnknownHostException(epoint.toString());
                }
                connectV4(in, out, epoint, deadlineMillis2);
            } else if (epoint.isUnresolved()) {
                throw new UnknownHostException(epoint.toString());
            } else {
                connectV4(in, out, epoint, deadlineMillis2);
            }
        } catch (IOException e2) {
            throw new SocketException(e2.getMessage());
        }
    }

    protected InetAddress getInetAddress() {
        if (this.external_address != null) {
            return this.external_address.getAddress();
        }
        return super.getInetAddress();
    }

    protected int getPort() {
        if (this.external_address != null) {
            return this.external_address.getPort();
        }
        return super.getPort();
    }

    protected int getLocalPort() {
        if (this.socket != null) {
            return super.getLocalPort();
        }
        if (this.external_address != null) {
            return this.external_address.getPort();
        }
        return super.getLocalPort();
    }

    protected void close() throws IOException {
        if (this.cmdsock != null) {
            this.cmdsock.close();
        }
        this.cmdsock = null;
        super.close();
    }

    private String getUserName() {
        String userName = "";
        if (!this.applicationSetProxy) {
            return (String) AccessController.doPrivileged(new GetPropertyAction("user.name"));
        }
        try {
            return System.getProperty("user.name");
        } catch (SecurityException e) {
            return userName;
        }
    }
}
