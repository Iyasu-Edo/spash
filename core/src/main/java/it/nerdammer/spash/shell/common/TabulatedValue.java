package it.nerdammer.spash.shell.common;

import ch.lambdaj.Lambda;
import ch.lambdaj.function.convert.StringLengthConverter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Nicola Ferraro
 */
public class TabulatedValue implements Iterable<String> {

    private List<String> values;

    public TabulatedValue() {
        this.values = new ArrayList<>();
    }

    public void set(int pos, String value) {
        this.values.set(pos, value);
    }

    public String remove(int pos) {
        return this.values.remove(pos);
    }

    public void add(String value) {
        this.values.add(value);
    }

    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }

    public String toString(List<Integer> columnSizes) {
        if(columnSizes.size()!=this.values.size()) {
            throw new IllegalArgumentException("Size list and value list must have the same length: " + this.values.size() + "/" + columnSizes.size());
        }

        Iterator<Integer> sizes = columnSizes.iterator();
        Iterator<String> strs = this.iterator();

        StringBuilder bui = new StringBuilder();

        while(sizes.hasNext() && strs.hasNext()) {
            String str = strs.next();
            int size = sizes.next();
            bui.append(StringUtils.rightPad(str, size));

            if(strs.hasNext()) {
                bui.append(" ");
            }
        }

        return bui.toString();
    }

    public List<Integer> columnSizes() {
        return Lambda.convert(this.values, new StringLengthConverter());
    }

    public static List<Integer> combineColumnSizes(List<Integer> s1, List<Integer> s2) {
        Iterator<Integer> i1 = s1.iterator();
        Iterator<Integer> i2 = s2.iterator();
        List<Integer> res = new ArrayList<>(s1.size());
        while(i1.hasNext() && i2.hasNext()) {
            int v = Math.max(i1.next(), i2.next());
            res.add(v);
        }

        if(i1.hasNext() || i2.hasNext()) {
            throw new IllegalArgumentException("Collections have different sizes");
        }

        return res;
    }

}
