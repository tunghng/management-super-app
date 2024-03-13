package com.im.announcement.controller;

public class ControllerConstants {
    protected static final String PAGE_DATA_PARAMETERS = "You can specify parameters to filter the results. " +
            "The result is wrapped with PageData object that allows you to iterate over result set using pagination. ";
    protected static final String PAGE_SIZE_DESCRIPTION = "Maximum amount of entities in a one page";
    protected static final String PAGE_NUMBER_DESCRIPTION = "Sequence number of page starting from 0";
    protected static final String ANNOUNCEMENT_ID_PARAM_DESCRIPTION = "A string value representing the announcement id. For example, '784f394c-42b6-435a-983c-b7beff2784f9'";
    protected static final String ANNOUNCEMENT_IS_READ_PARAM_DESCRIPTION = "A boolean value representing the announcement isRead flag.";
    protected static final String ANNOUNCEMENT_IS_DELETED_PARAM_DESCRIPTION = "A boolean value representing the announcement isDeleted flag.";
    protected static final String ANNOUNCEMENT_TEXT_SEARCH_DESCRIPTION = "The case insensitive 'substring' filter based on the announcement headline.";

}