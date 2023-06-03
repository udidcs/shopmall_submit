package com.example.demo;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;

@Data
public class Trie {
    private HashMap<Character, Object> root = new HashMap<>();
    private HashMap<Character, Object> cur;
    private String keyword;

    public void add(String word) {
        cur = root;
        for (char c : word.toCharArray()) {
            if (cur.containsKey(c)) {
                cur = (HashMap<Character, Object>)cur.get(c);
                continue;
            }
            else {
                cur.put(c, new HashMap<>());
                cur = (HashMap<Character, Object>)cur.get(c);
            }
        }
        Integer num = (Integer)cur.get('*');
        if (num==null)
            cur.put('*', 0);
        else
            cur.put('*', num+1);

    }


    public ArrayList<String> getAllWords(HashMap<Character, Object> cur) {
        ArrayList<String> st = new ArrayList<>();
        ArrayList<Character> tmp = new ArrayList<>();
        getAllWordsRecur(cur, st, tmp);

        for (int i = 0; i < st.size(); i++) {
            st.set(i, keyword + st.get(i));
        }
        return st;
    }

    private void getAllWordsRecur(HashMap<Character, Object> cur, ArrayList<String> st, ArrayList<Character> tmp) {
        for (Character character : cur.keySet()) {
            if (cur.get(character) instanceof Integer) {
                String str = "";
                for (Character c : tmp) {
                    str += c;
                }
                st.add(str);
            }
            else {
                tmp.add(character);
                getAllWordsRecur((HashMap<Character, Object>) cur.get(character), st, tmp);
                tmp.remove(tmp.size()-1);
            }
        }
    }

    public HashMap<Character, Object> findNode(String word) {
        keyword = word;
        HashMap<Character, Object> cur = root;
        for (char c : word.toCharArray()) {
            if (cur.containsKey(c)) {
                cur = (HashMap<Character, Object>) cur.get(c);
            }
            else {
                return null;
            }
        }
        return cur;
    }


}


