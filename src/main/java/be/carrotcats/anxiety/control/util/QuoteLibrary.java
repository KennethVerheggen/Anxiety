/*
 * Copyright 2016 Kenneth Verheggen <kenneth.verheggen@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.carrotcats.anxiety.control.util;

import be.carrotcats.anxiety.control.util.scene.InformationScreenBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class QuoteLibrary {

    /**
     * List of quotes
     */
    private final ArrayList<String> quotes = new ArrayList<>();
    /**
     * Object for random generated index
     */
    private static final Random RANDOM = new Random();
    /**
     * the path to the quotes file
     */
    public static final String quotesFilePath = "/info/quotes.txt";

    public QuoteLibrary() throws IOException {
        init(quotesFilePath);
    }

    /**
     * Constructor for a quote library
     * @param path the path to the quote files
     * @throws IOException
     */
    public QuoteLibrary(String path) throws IOException {
        init(path);
    }

    private void init(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(InformationScreenBuilder.class.getResourceAsStream(path),"UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    quotes.add(line);
                }
            }
        }
    }

    public ArrayList<String> getQuotes() {
        return quotes;
    }

    /**
     *
     * @return a random quote
     */
    public String getRandomQuote() {
        Collections.shuffle(quotes);
        return quotes.get(RANDOM.nextInt(quotes.size()));
    }

}
