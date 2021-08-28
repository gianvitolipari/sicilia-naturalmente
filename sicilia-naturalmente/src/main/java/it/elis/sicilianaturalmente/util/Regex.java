package it.elis.sicilianaturalmente.util;

import java.util.regex.Pattern;

public class Regex {
    public static final Pattern VALID_ONLY_LETTERS= Pattern.compile("^^[a-zA-Z-]{2,25}$");
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_MIN_AND_MAX_SIZE = Pattern.compile("^.{6,25}$");
    public static final Pattern VALID_ONLY_LETTERS_OR_EMPTY= Pattern.compile("^^[a-zA-Z-]{2,25}$|^^.{0,0}$");
}
