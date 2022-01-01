package me.alluseri.fareen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static String fallback(Scanner s) {
		System.out.println("Please enter the code you want to execute.");
		return s.nextLine();
	}
	public static void main(String args[]) throws FileNotFoundException {
		Scanner s = new Scanner(System.in);
		String code = "";
		int cell = 0;
		int[] cells = new int[1024];
		if (args.length >= 2)
			switch (args[0]) { // lmao ik this is awful
			case "--code":
				code = args[1];
				break;
			case "--file":
				StringBuilder sb = new StringBuilder();
				for (int i = 1;i < args.length;i++)
					sb.append(args[i]);
				File f = new File(sb.toString());
				if (!f.exists() || f.isDirectory() || !f.canRead()) {
					code = fallback(s);
					break;
				}
				try (BufferedReader fr = new BufferedReader(new FileReader(f))) { // Shouldn't throw FNFE cuz f.exists() check
					code = fr.readLine();
				} catch (IOException e1) {
					System.out.println("An error has occured while reading your input file:");
					e1.printStackTrace();
					s.close();
					return;
				}
				break;
			default:
				code = fallback(s);
				break;
			}
		else code = fallback(s);
		code = code.trim().replaceAll("\\s", "");
		if (code.matches("\\D")) {
			System.out.println("The input code is malformed!");
			return;
		}
		System.out.println("Executing "+code+"\n");
		String[] spl = code.split("");
		for (int e = 0; e < code.length();) {
			switch (code.toCharArray()[e]) {
			case '0':
				break; // NOP
			case '1': // Next memory cell
				cell++;
				break;
			case '2': // Previous memory cell
				cell--;
				break;
			case '3': // Add 1 to memory in cell
				cells[cell]++;
				break;
			case '4': // Subtract 1 from memory in cell
				cells[cell]--;
				break;
			case '5': { // Jump to execution index at next (e+1) numbers
				int next = Integer.parseInt(spl[++e]);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < next; i++)
					sb.append(spl[++e]);
				e = Integer.parseInt(sb.toString());
			} continue;
			case '6': { // If current memory cell == value at next (e+1) numbers, jump at execution index at next (e+2) numbers after (e+1) numbers
				int next_mem = Integer.parseInt(spl[++e]);
				int next_exec = Integer.parseInt(spl[++e]);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < next_mem; i++)
					sb.append(spl[++e]);
				int requestedValue = Integer.parseInt(sb.toString());
				sb.setLength(0);
				for (int i = 0; i < next_exec; i++)
					sb.append(spl[++e]);
				int jumpTarget = Integer.parseInt(sb.toString());
				sb = null;
				if (cells[cell] == requestedValue) {
					e = jumpTarget;
					continue;
				}
			} break;
			case '7': { // If current memory cell != value at next (e+1) numbers, jump at execution index at next (e+2) numbers after (e+1) numbers
				int next_mem = Integer.parseInt(spl[++e]);
				int next_exec = Integer.parseInt(spl[++e]);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < next_mem; i++)
					sb.append(spl[++e]);
				int requestedValue = Integer.parseInt(sb.toString());
				sb.setLength(0);
				for (int i = 0; i < next_exec; i++)
					sb.append(spl[++e]);
				int jumpTarget = Integer.parseInt(sb.toString());
				sb = null;
				if (cells[cell] != requestedValue) {
					e = jumpTarget;
					continue;
				}
			} break;
			case '8':
				System.out.print((char) cells[cell]);
				break;
			case '9':
				System.out.println("this wishes for input.");
				cells[cell] = s.nextInt();
				break;
			}
			e++;
		}
		System.out.println("\n\nFinished execution, dumping memory...");
		int res = 0;
		StringBuilder o = new StringBuilder("[");
		for (int cell2 : cells)
			if (cell2 != 0) {
				if (res == 1) {
					o.append("0,");
					res = 0;
				} else if (res > 0) {
					o.append("(..");
					o.append(res);
					o.append("..),");
					res = 0;
				}
				o.append(cell2);
				o.append(",");
			} else
				res++;
		if (res == 1)
			o.append("0]");
		else if (res > 0) {
			o.append("(..");
			o.append(res);
			o.append("..)]");
		} else {
			o.setLength(o.length() - 1);
			o.append("]");
		}
		System.out.println("Memory dump(n0 cells only):");
		System.out.println(o.toString());
		s.close();
	}
}
