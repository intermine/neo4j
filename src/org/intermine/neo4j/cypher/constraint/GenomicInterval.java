package org.intermine.neo4j.cypher.constraint;

import java.util.regex.Pattern;

/**
 * Represents a genomic interval
 *
 * Source Location - org.intermine.bio.query.range.ChromosomeLocationHelper
 */
public class GenomicInterval {

    private final Integer start, end;
    private final String chr, taxonId;
    private final String parsedAs;

    private static final Pattern GFF3 = Pattern.compile(
    "^[^\\t]+\\t[^\\t]+\\t[^\\t]+\\d+\\t\\d+");
    private static final Pattern BED = Pattern.compile("^[^\\t]+\\t\\d+\\t\\d+");
    private static final Pattern COLON_DASH = Pattern.compile("^[^:]+:\\d+-\\d+");
    private static final Pattern COLON_DOTS = Pattern.compile("^[^:]+:\\d+\\.\\.\\.?\\d+");
    private static final Pattern COLON_START = Pattern.compile("^[^:]+:\\d+$");
    private static final Pattern CHR_ONLY = Pattern.compile("^[^:]+$");
    private static final Pattern COLON_DASH_WITH_TAXON
    = Pattern.compile("^\\d+:[^:]+:\\d+-\\d+");
    private static final Pattern COLON_DOTS_WITH_TAXON
    = Pattern.compile("^\\d+:[^:]+:\\d+\\.\\.\\.?\\d+");

    /**
     * @param range string to be parsed for coordinates
     */
    public GenomicInterval(String range) {
        if (range == null) {
            throw new NullPointerException("range may not be null");
        }
        if (GFF3.matcher(range).find()) {
            String[] parts = range.split("\\t");
            chr = parts[0].trim();
            start = Integer.valueOf(parts[3].trim());
            end = Integer.valueOf(parts[4].trim());
            taxonId = null;
            parsedAs = "GFF3";
        } else if (BED.matcher(range).find()) {
            String[] parts = range.split("\\t");
            chr = parts[0].trim();
            start = Integer.valueOf(parts[1].trim());
            end = Integer.valueOf(parts[2].trim());
            taxonId = null;
            parsedAs = "BED";
        } else if (COLON_DASH.matcher(range).matches()) {
            String[] partsA = range.split(":");
            chr = partsA[0];
            String[] partsB = partsA[1].split("-");
            start = Integer.valueOf(partsB[0].trim());
            end = Integer.valueOf(partsB[1].trim());
            taxonId = null;
            parsedAs = "COLON_DASH";
        } else if (COLON_DOTS.matcher(range).matches()) {
            String[] partsA = range.split(":");
            chr = partsA[0];
            String[] partsB = partsA[1].split("\\.\\.");
            start = Integer.valueOf(partsB[0].trim());
            String rawEnd = partsB[1].trim();
            if (rawEnd.startsWith(".")) {
                rawEnd = rawEnd.substring(1);
            }
            end = Integer.valueOf(rawEnd);
            taxonId = null;
            parsedAs = "COLON_DOTS";
        } else if (COLON_DOTS_WITH_TAXON.matcher(range).matches()) {
            String[] partsA = range.split(":");
            taxonId = partsA[0];
            chr = partsA[1];
            String[] partsB = partsA[2].split("\\.\\.");
            start = Integer.valueOf(partsB[0].trim());
            String rawEnd = partsB[1].trim();
            if (rawEnd.startsWith(".")) {
                rawEnd = rawEnd.substring(1);
            }
            end = Integer.valueOf(rawEnd);
            parsedAs = "COLON_DOTS_WITH_TAXON";
        } else if (COLON_DASH_WITH_TAXON.matcher(range).matches()) {
            String[] partsA = range.split(":");
            taxonId = partsA[0];
            chr = partsA[1];
            String[] partsB = partsA[2].split("-");
            start = Integer.valueOf(partsB[0].trim());
            end = Integer.valueOf(partsB[1].trim());
            parsedAs = "COLON_DASH_WITH_TAXON";
        } else if (COLON_START.matcher(range).matches()) {
            String[] partsA = range.split(":");
            chr = partsA[0];
            start = Integer.valueOf(partsA[1].trim());
            end = start;
            taxonId = null;
            parsedAs = "POINT";
        } else if (CHR_ONLY.matcher(range).matches()) {
            chr = range;
            start = null;
            end = null;
            taxonId = null;
            parsedAs = "CHR";
        } else {
            throw new IllegalArgumentException("Illegal range: " + range);
        }
        if (start != null && end != null && start > end) {
            throw new IllegalArgumentException(
            "Illegal range - start is greater than end: " + range);
        }
    }

    /**
     * @return start of range
     */
    public Integer getStart() {
        return start;
    }

    /**
     * @return end of range
     */
    public Integer getEnd() {
        return end;
    }

    /**
     * @return identifier of chromosome
     */
    public String getChr() {
        return chr;
    }

    /** @return the taxon ID for this region, if there is one. **/
    public String getTaxonId() {
        return taxonId;
    }

    /** @return what we interpreted this range as **/
    public String getParsedAs() {
        return parsedAs;
    }
}
