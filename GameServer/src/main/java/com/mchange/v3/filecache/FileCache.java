package com.mchange.v3.filecache;

import com.mchange.v1.io.InputStreamUtils;
import com.mchange.v1.io.OutputStreamUtils;
import com.mchange.v2.io.DirectoryDescentUtils;
import com.mchange.v2.io.FileIterator;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class FileCache {
    static final MLogger logger = MLog.getLogger(FileCache.class);
    static final FileFilter NOT_DIR_FF = new FileFilter() {
        public boolean accept(File param1File) {
            return !param1File.isDirectory();
        }
    };
    final File cacheDir;
    final int buffer_size;
    final boolean read_only;
    final List<URLFetcher> fetchers;

    public FileCache(File paramFile, int paramInt, boolean paramBoolean) throws IOException {
        this(paramFile, paramInt, paramBoolean, Collections.singletonList(URLFetchers.DEFAULT));
    }

    public FileCache(File paramFile, int paramInt, boolean paramBoolean, URLFetcher... paramVarArgs) throws IOException {
        this(paramFile, paramInt, paramBoolean, Arrays.asList(paramVarArgs));
    }

    public FileCache(File paramFile, int paramInt, boolean paramBoolean, List<URLFetcher> paramList) throws IOException {
        this.cacheDir = paramFile;
        this.buffer_size = paramInt;
        this.read_only = paramBoolean;

        this.fetchers = Collections.unmodifiableList(paramList);

        if (paramFile.exists()) {

            if (!paramFile.isDirectory()) {
                loggedIOException(MLevel.SEVERE, paramFile + "exists and is not a directory. Can't use as cacheDir.");
            } else if (!paramFile.canRead()) {
                loggedIOException(MLevel.SEVERE, paramFile + "must be readable.");
            } else if (!paramFile.canWrite() && !paramBoolean) {
                loggedIOException(MLevel.SEVERE, paramFile + "not writable, and not read only.");
            }
        } else if (!paramFile.mkdir()) {
            loggedIOException(MLevel.SEVERE, paramFile + "does not exist and could not be created.");
        }
    }

    private InputStream fetchURL(URL paramURL) throws IOException {
        LinkedList<IOException> linkedList = null;
        for (URLFetcher uRLFetcher : this.fetchers) {
            try {
                return uRLFetcher.openStream(paramURL, logger);
            } catch (FileNotFoundException fileNotFoundException) {
                throw fileNotFoundException;
            } catch (IOException iOException) {

                if (logger.isLoggable(MLevel.FINE))
                    logger.log(MLevel.FINE, "URLFetcher " + uRLFetcher + " failed on Exception. Will try next fetcher, if any.", iOException);
                if (linkedList == null)
                    linkedList = new LinkedList();
                linkedList.add(iOException);
            }
        }
        if (logger.isLoggable(MLevel.WARNING)) {

            logger.log(MLevel.WARNING, "All URLFetchers failed on URL " + paramURL);
            byte b;
            int i;
            for (b = 0, i = linkedList.size(); b < i; b++) {
                logger.log(MLevel.WARNING, "URLFetcher Exception #" + (b + 1), linkedList.get(b));
            }
        }

        throw new IOException("Failed to fetch URL '" + paramURL + "'.");
    }

    public void ensureCached(FileCacheKey paramFileCacheKey, boolean paramBoolean) throws IOException {
        File file = file(paramFileCacheKey);

        if (!this.read_only) {

            if (paramBoolean || !file.exists()) {

                BufferedInputStream bufferedInputStream = null;
                BufferedOutputStream bufferedOutputStream = null;

                try {
                    if (logger.isLoggable(MLevel.FINE))
                        logger.log(MLevel.FINE, "Caching file for " + paramFileCacheKey + " to " + file.getAbsolutePath() + "...");
                    File file1 = file.getParentFile();
                    if (!file1.exists()) file1.mkdirs();

                    bufferedInputStream = new BufferedInputStream(fetchURL(paramFileCacheKey.getURL()), this.buffer_size);
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), this.buffer_size);
                    int i;
                    for (i = bufferedInputStream.read(); i >= 0; i = bufferedInputStream.read()) {
                        bufferedOutputStream.write(i);
                    }
                    if (logger.isLoggable(MLevel.INFO)) {
                        logger.log(MLevel.INFO, "Cached file for " + paramFileCacheKey + ".");
                    }
                } catch (IOException iOException) {

                    logger.log(MLevel.WARNING, "An exception occurred while caching file for " + paramFileCacheKey + ". Deleting questionable cached file.", iOException);
                    file.delete();
                    throw iOException;
                } finally {

                    InputStreamUtils.attemptClose(bufferedInputStream);
                    OutputStreamUtils.attemptClose(bufferedOutputStream);

                }

            } else if (logger.isLoggable(MLevel.FINE)) {
                logger.log(MLevel.FINE, "File for " + paramFileCacheKey + " already exists and force_reacquire is not set.");
            }
        } else {
            if (paramBoolean) {

                String str = "force_reacquire canot be set on a read_only FileCache.";
                IllegalArgumentException illegalArgumentException = new IllegalArgumentException(str);
                logger.log(MLevel.WARNING, str, illegalArgumentException);
                throw illegalArgumentException;
            }
            if (!file.exists()) {

                String str = "Cache is read only, and file for key '" + paramFileCacheKey + "' does not exist.";
                FileNotCachedException fileNotCachedException = new FileNotCachedException(str);
                logger.log(MLevel.FINE, str, fileNotCachedException);
                throw fileNotCachedException;
            }
        }
    }

    public InputStream fetch(FileCacheKey paramFileCacheKey, boolean paramBoolean) throws IOException {
        ensureCached(paramFileCacheKey, paramBoolean);
        return new FileInputStream(file(paramFileCacheKey));
    }

    public boolean isCached(FileCacheKey paramFileCacheKey) throws IOException {
        return file(paramFileCacheKey).exists();
    }

    public int countCached() throws IOException {
        byte b = 0;
        for (FileIterator fileIterator = DirectoryDescentUtils.depthFirstEagerDescent(this.cacheDir, NOT_DIR_FF, false); fileIterator.hasNext(); ) {

            fileIterator.next();
            b++;
        }
        return b;
    }

    public int countCached(FileFilter paramFileFilter) throws IOException {
        byte b = 0;
        for (FileIterator fileIterator = DirectoryDescentUtils.depthFirstEagerDescent(this.cacheDir, new NotDirAndFileFilter(paramFileFilter), false); fileIterator.hasNext(); ) {

            fileIterator.next();
            b++;
        }
        return b;
    }

    public File fileForKey(FileCacheKey paramFileCacheKey) {
        return file(paramFileCacheKey);
    }

    private File file(FileCacheKey paramFileCacheKey) {
        return new File(this.cacheDir, paramFileCacheKey.getCacheFilePath());
    }

    private void loggedIOException(MLevel paramMLevel, String paramString) throws IOException {
        IOException iOException = new IOException(paramString);
        logger.log(paramMLevel, paramString, iOException);
        throw iOException;
    }

    static class NotDirAndFileFilter implements FileFilter {
        FileFilter ff;

        NotDirAndFileFilter(FileFilter param1FileFilter) {
            this.ff = param1FileFilter;
        }

        public boolean accept(File param1File) {
            return (!param1File.isDirectory() && this.ff.accept(param1File));
        }
    }
}

