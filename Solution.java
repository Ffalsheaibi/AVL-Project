import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

import java.util.*;
import java.util.regex.*;

public class Solution {
static final Pattern TOKEN = Pattern.compile("[A-Za-z0-9]+");

static String clean(String t) {
return t.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
}

public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
Index idx = new Index();

while (sc.hasNextLine()) {
String line = sc.nextLine();
if (line == null) break;
line = line.trim();
if (line.isEmpty()) continue;

String[] parts = line.split("\\s+");
int cmd;
try { cmd = Integer.parseInt(parts[0]); }
catch (Exception e) { System.out.println(-1); return; }

if (cmd < 1 || cmd > 4) { System.out.println(-1); return; }

if (cmd == 1) {
if (parts.length < 2) { System.out.println(-1); return; }
int s;
try { s = Integer.parseInt(parts[1]); }
catch (Exception e) { System.out.println(-1); return; }
if (s < 1) { System.out.println(-1); return; }

for (int j = 0; j < s; j++) {
if (!sc.hasNextLine()) { System.out.println(-1); return; }
String header = sc.nextLine();
if (header == null) { System.out.println(-1); return; }
header = header.trim();
while (header.isEmpty() && sc.hasNextLine()) {
header = sc.nextLine();
if (header == null) { System.out.println(-1); return; }
header = header.trim();
}

int lastSpace = header.lastIndexOf(' ');
if (lastSpace < 0) { System.out.println(-1); return; }
String fname = header.substring(0, lastSpace).trim();
String numStr = header.substring(lastSpace + 1).trim();
int n;
try { n = Integer.parseInt(numStr); }
catch (Exception e) { System.out.println(-1); return; }
if (n < 1 || fname.length() == 0) { System.out.println(-1); return; }

for (int k = 1; k <= n; k++) {
if (!sc.hasNextLine()) { System.out.println(-1); return; }
String raw = sc.nextLine();
if (raw == null) raw = "";
Matcher m = TOKEN.matcher(raw);
while (m.find()) {
String tok = clean(m.group());
if (!tok.isEmpty()) idx.insert(tok, fname, k);
}
}
}

} else if (cmd == 2) {
if (idx.root == null) { System.out.println(-1); continue; }
if (parts.length < 2) { System.out.println(-1); continue; }
String q = clean(parts[1]);
if (q.isEmpty()) { System.out.println(-1); continue; }
idx.search(q);

} else if (cmd == 3) {
if (parts.length < 2) { System.out.println(-1); continue; }
String q = clean(parts[1]);
if (q.isEmpty()) { System.out.println(-1); continue; }
if (idx.root == null) { System.out.println(0); continue; }
if (!idx.contains(q)) { System.out.println(0); continue; }
idx.remove(q);

} else if (cmd == 4) {
if (idx.root == null) { System.out.println(-1); continue; }
idx.traversePostOrder();
}
}
sc.close();
}
}

class Index {
AVLNode root;

void insert(String t, String f, int l) { root = insertNode(root, t, f, l); }

private AVLNode insertNode(AVLNode node, String t, String f, int l) {
if (node == null) {
AVLNode nn = new AVLNode(t);
nn.list.add(new ListNode(f, l));
nn.freq = 1;
return nn;
}
int cmp = t.compareTo(node.token);
if (cmp < 0) node.left = insertNode(node.left, t, f, l);
else if (cmp > 0) node.right = insertNode(node.right, t, f, l);
else {
node.freq++;
nnListAdd(node.list, f, l);
return node;
}
node.height = 1 + Math.max(h(node.left), h(node.right));
int bal = bal(node);
if (bal > 1 && t.compareTo(node.left.token) < 0) return rotR(node);
if (bal < -1 && t.compareTo(node.right.token) > 0) return rotL(node);
if (bal > 1 && t.compareTo(node.left.token) > 0) { node.left = rotL(node.left); return rotR(node); }
if (bal < -1 && t.compareTo(node.right.token) < 0) { node.right = rotR(node.right); return rotL(node); }
return node;
}

private void nnListAdd(SList list, String f, int l) {
list.add(new ListNode(f, l));
}

void remove(String t) { root = removeNode(root, t); }

private AVLNode removeNode(AVLNode node, String t) {
if (node == null) return null;
int cmp = t.compareTo(node.token);
if (cmp < 0) node.left = removeNode(node.left, t);
else if (cmp > 0) node.right = removeNode(node.right, t);
else {
if (node.left == null && node.right == null) node = null;
else if (node.left == null) node = node.right;
else if (node.right == null) node = node.left;
else {
AVLNode s = min(node.right);
node.token = s.token;
node.freq = s.freq;
node.list = s.list;
node.right = removeNode(node.right, s.token);
}
}
if (node == null) return null;
node.height = 1 + Math.max(h(node.left), h(node.right));
int bal = bal(node);
if (bal > 1 && bal(node.left) >= 0) return rotR(node);
if (bal > 1 && bal(node.left) < 0) { node.left = rotL(node.left); return rotR(node); }
if (bal < -1 && bal(node.right) <= 0) return rotL(node);
if (bal < -1 && bal(node.right) > 0) { node.right = rotR(node.right); return rotL(node); }
return node;
}

void search(String t) {
AVLNode n = searchNode(root, t);
if (n == null) { System.out.println(0); return; }
System.out.println(n.freq);
ListNode c = n.list.head;
while (c != null) {
System.out.println(c.file + " " + c.line);
c = c.next;
}
}

boolean contains(String t) { return searchNode(root, t) != null; }

private AVLNode searchNode(AVLNode n, String t) {
if (n == null) return null;
int cmp = t.compareTo(n.token);
if (cmp == 0) return n;
if (cmp < 0) return searchNode(n.left, t);
return searchNode(n.right, t);
}

void traversePostOrder() {
StringBuilder sb = new StringBuilder();
post(root, sb);
System.out.println(sb.toString());
}

private void post(AVLNode n, StringBuilder sb) {
if (n == null) return;
post(n.left, sb);
post(n.right, sb);
if (sb.length() > 0) sb.append(' ');
sb.append(n.token);
}

private int h(AVLNode n) { return (n == null) ? -1 : n.height; }
private int bal(AVLNode n) { return (n == null) ? 0 : h(n.left) - h(n.right); }

private AVLNode rotR(AVLNode y) {
AVLNode x = y.left, t = x.right;
x.right = y; y.left = t;
y.height = 1 + Math.max(h(y.left), h(y.right));
x.height = 1 + Math.max(h(x.left), h(x.right));
return x;
}
private AVLNode rotL(AVLNode x) {
AVLNode y = x.right, t = y.left;
y.left = x; x.right = t;
x.height = 1 + Math.max(h(x.left), h(x.right));
y.height = 1 + Math.max(h(y.left), h(y.right));
return y;
}
private AVLNode min(AVLNode n) { while (n.left != null) n = n.left; return n; }
}

class AVLNode {
String token;
int height, freq;
SList list;
AVLNode left, right;
AVLNode(String t) { token = t; height = 0; freq = 0; list = new SList(); }
}

class SList {
int size;
ListNode head, tail;
void add(ListNode n) {
if (head == null) head = tail = n;
else { tail.next = n; tail = n; }
size++;
}
}

class ListNode {
String file; int line; ListNode next;
ListNode(String f, int l) { file = f; line = l; }
}
