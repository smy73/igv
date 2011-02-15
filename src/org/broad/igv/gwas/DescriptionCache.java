package org.broad.igv.gwas;

import org.apache.log4j.Logger;
import org.broad.igv.PreferenceManager;
import org.broad.igv.util.ParsingUtils;

import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: jussi
 * Date: 2/7/11
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DescriptionCache {

    private static final Logger log = Logger.getLogger(DescriptionCache.class);


    // Maximum amount of values stored in the cache
    private int maxSize;

    private ArrayList<String> chrs = new ArrayList<String>();
    private ArrayList<Integer> locations = new ArrayList<Integer>();
    private ArrayList<Float> values = new ArrayList<Float>();
    private ArrayList<String> descriptions = new ArrayList<String>();
    // Storage for the header tokens
    private String[] headerTokens = new String[1000];

    public DescriptionCache() {

        PreferenceManager prefs = PreferenceManager.getInstance();
        this.maxSize = prefs.getAsInt(PreferenceManager.GWAS_DESCRIPTION_CACHE_SIZE);

    }

    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Set size of the cache in lines. Minimum size is 10.
     * @param maxSize
     */
    public void setMaxSize(int maxSize) {
        if (maxSize < 10)
            maxSize = 10;
        this.maxSize = maxSize;
    }

    public String[] getHeaderTokens() {
        return headerTokens;
    }

    public void setHeaderTokens(String[] headerTokens) {
        this.headerTokens = headerTokens;
    }

    public void setHeaderTokens(String headerString) {
        headerString = headerString.trim();
        //ParsingUtils.splitSpaces(headerString, this.headerTokens);
        ParsingUtils.splitWhitespace(headerString, this.headerTokens);


    }


    public void clear() {

        this.chrs = new ArrayList<String>();
        this.locations = new ArrayList<Integer>();
        this.descriptions = new ArrayList<String>();
    }


    public boolean add(String chr, int location, float value, String description) {

        if (this.locations.size() >= this.maxSize) {
            this.locations.remove(0);
            this.chrs.remove(0);
            this.descriptions.remove(0);
            this.values.remove(0);
        }

        return this.chrs.add(chr) & this.locations.add(location) & this.descriptions.add(description) & this.values.add(value);
    }

    public String getDescription(String chr, int location, float value) {

        String description = null;

        int indexCounter = 0;
        boolean descriptionFound = false;

        while (indexCounter < this.descriptions.size() && !descriptionFound) {
            if (this.chrs.get(indexCounter).equals(chr) && this.locations.get(indexCounter) == location && this.values.get(indexCounter) == value) {
                description = this.descriptions.get(indexCounter);
                descriptionFound = true;

            }
            indexCounter++;
        }

        return description;

    }


    public String getDescriptionString(String chr, int location, float value) {

        String description = this.getDescription(chr, location, value);
        String descriptionString = null;

        if (description != null) {
            descriptionString = "";
            int headersSize = this.getHeaderTokens().length;
            String[] tokens = new String[1000];

            //ParsingUtils.splitSpaces(description, tokens);
            ParsingUtils.splitWhitespace(description, tokens);

            for (int i = 0; i < headersSize; i++) {
                String tmpHeaderToken = this.getHeaderTokens()[i];
                if (tmpHeaderToken != null)
                    descriptionString += tmpHeaderToken + ": " + tokens[i] + "<br>";
            }

        }
        return descriptionString;

    }


}
