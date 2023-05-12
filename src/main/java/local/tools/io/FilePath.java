package local.tools.io;

import java.net.MalformedURLException;
import java.net.URL;
import local.tools.logs.Logger;
import local.tools.utils.StringUtils;

public class FilePath {
    private String path = "";
    private String filename = "";
    private String extension = "";
    private boolean hasDot = false;

    public FilePath() {
    }
    
    public FilePath(String filepath) {
        filepath = filepath.contains("?") ? filepath.substring(0, filepath.lastIndexOf("?")) : filepath;
        String file = filepath;
        int last_slash_pos = Math.max(filepath.lastIndexOf('\\'), filepath.lastIndexOf('/'));
        if (last_slash_pos != -1) {
            this.path = filepath.substring(0, last_slash_pos + 1);
            if (last_slash_pos <= filepath.length() - 2) {
                file = filepath.substring(last_slash_pos + 1);
            }
        }

        int dot = file.lastIndexOf('.');
        if (dot != -1) {
            this.filename = file.substring(0, dot);
            this.extension = file.substring(dot + 1);
            this.hasDot = true;
        } else {
            this.filename = file;
            this.hasDot = false;
        }
    }
    
    public static FilePath parse(String filepath) {
        return new FilePath(filepath);
    }
    
    public static FilePath parseURL(String url) {
        FilePath item = new FilePath();
        try {
            URL endpoint = new URL(PathUtils.decodeURL(url));
            String filepath = parse(endpoint.getFile()).filename();

            int pos = filepath.indexOf('?');
            if (pos != -1) {
                filepath = filepath.substring(0, pos);
            }

            return parse(filepath);
        } catch (MalformedURLException e) {
            Logger.error("Unable to parse url_path: " + e.getMessage());
        }
        return item;
    }

    public String filepath() {
        StringBuilder sb = new StringBuilder();
        sb.append(path).append(filename(extension));
        return sb.toString();
    }
    
    public String filepath(String newExt) {
        StringBuilder sb = new StringBuilder();
        sb.append(path).append(filename(newExt));
        return sb.toString();
    }
    
    public String filename(String newExt) {
        StringBuilder sb = new StringBuilder();
        sb.append(filename);
        if (StringUtils.isNotEmpty(newExt)) {
            sb.append('.');
            sb.append(newExt);
        } else if(hasDot) {
            sb.append('.');
        }
        return sb.toString();
    }

    public String filename() {
        return filename(extension);
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return filename;
    }

    public String getExt() {
        return extension;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setName(String name) {
        this.filename = name;
    }

    public void setExt(String ext) {
        this.extension = ext;
    }

    public boolean hasDot() {
        return hasDot || StringUtils.isNotEmpty(extension);
    }

    @Override
    public String toString() {
        return filepath();
    }
}
