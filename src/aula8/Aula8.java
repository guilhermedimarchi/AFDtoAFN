/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aula8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author Guilherme
 */
public class Aula8 {

	public static boolean runDfa(HashMap<Integer, HashMap<Character, Integer>> dfa, String input,
			List<Integer> finalStates) {

		int state = 0;

		for (char c : input.toCharArray()) {
			try {
				HashMap<Character, Integer> transitions = dfa.get(state);
				state = transitions.get(c);
			} catch (Exception e) {
				return false;
			}
		}

		return finalStates.contains(state);
	}

	public static void main(String[] args) {
	
	
		HashMap<Integer, HashMap<Character, ArrayList<Integer>>> nfa = new HashMap<>();
		
		HashMap<Character, ArrayList<Integer>> transitions = new HashMap<>();
                
		ArrayList<Integer> states = new ArrayList<>();
		states.add(0);
		transitions.put('a', states);
                
                states = new ArrayList<>();
		states.add(1);
		transitions.put('E', states);
                
		nfa.put(0, transitions);

                
                transitions = new HashMap<>();
                
		states = new ArrayList<>();
		states.add(1);
		transitions.put('b', states);
                
                states = new ArrayList<>();
		states.add(2);
		transitions.put('E', states);
                
		nfa.put(1, transitions);
                
                
                transitions = new HashMap<>();
                
		states = new ArrayList<>();
		states.add(2);
		transitions.put('a', states);
                
        
		nfa.put(2, transitions);
	
		HashSet<Character> alfabeto = new HashSet();
                alfabeto.add('a');
                alfabeto.add('b');
                alfabeto.add('E');
                
                HashSet<Integer> finais = new HashSet<>();
		finais.add(2);
               
		System.out.println("AFN: "+nfa);
                System.out.println("Nos finais AFN: "+finais);
		System.out.println("AFD: "+nfaToDfa(alfabeto,finais, nfa));
                System.out.println("Nos finais AFD: "+finais);
                
	}	
	
	        
        
	
	public static HashSet<Integer> closure(int state, HashMap<Integer, HashMap<Character, ArrayList<Integer>>> nfa) 
        {
		HashSet<Integer> closure = new HashSet<>();
		Stack<Integer> st = new Stack<>();
		
		closure.add(state);
		st.push(state);
		
		while (!st.isEmpty()) {
			int currentState = st.pop();
							
			HashMap<Character, ArrayList<Integer>> transitions = nfa.get(currentState);
			if (transitions.containsKey('E')) {
				ArrayList<Integer> emptyTransitions = transitions.get('E');
				
				for (int s : emptyTransitions) {
					if (!closure.contains(s)) {
						st.push(s);
						closure.add(s);
					}
				}
			}
		}
		return closure;
	}
        public static HashSet<Integer> edge(int state, char c,HashMap<Integer, HashMap<Character, ArrayList<Integer>>> nfa )
        {
                HashSet<Integer> edge = new HashSet<>();
                ArrayList<Integer> aux = nfa.get(state).get(c);
                
                if(aux!=null)
                    edge.addAll(aux);
		
		return edge;            
        }
        public static HashSet<Integer> DFAedge(HashSet<Integer> states, char c,HashMap<Integer, HashMap<Character, ArrayList<Integer>>> nfa )
        {
            
            HashSet<Integer> edge = new HashSet();
            
            for( int state : (states))
            {
                if(state<nfa.size())
                {
                    //if(alfabeto.contains('E'))
                    edge.addAll(edge(state,'E',nfa));   
                    edge.addAll(edge(state,c,nfa));
                }
            }

            return edge;            
        }
        public static HashMap<Integer, HashMap<Character,Integer>> nfaToDfa(HashSet<Character> alfabeto,HashSet<Integer> finais,HashMap<Integer, HashMap<Character, ArrayList<Integer>>> nfa )
        {
           // Object retorno = new Object[2];
            HashSet<Integer> finaisDFA = (HashSet<Integer>)finais.clone();
           // finais.clear();
            int count =0 ;
            //Stack<HashSet<Integer>> stack = new Stack();
           Queue<HashSet<Integer>> queue = new LinkedList<HashSet<Integer>>();
            HashMap<Integer, HashMap<Character,Integer>> dfa = new HashMap();
            HashMap<Integer, HashSet<Integer>> list = new HashMap();

            HashMap<Character,Integer> aux2 = new HashMap();
            HashSet<Integer> aux3;
            
            HashSet<Integer> aux = closure(0, nfa);
            
            list.put(0, aux);
            queue.add(aux);
            
            while(!queue.isEmpty())
            {
                aux = queue.poll();
                
                for(int i=0;i<alfabeto.size();i++)
                {
                    char c = (char) alfabeto.toArray()[i];   
                    aux3 = DFAedge(aux, c, nfa);
                    
                    for(int f:finaisDFA)
                    {
                        if(aux.contains(f))
                            finais.add(count);     
                    }
        
                    
                    int pos  = verificaSeJaExiste(list, aux3);
                    
                    if(pos==list.size())
                    {
                        list.put(pos, aux3);
                        queue.add(aux3);
                    }
                    
                   
                    aux2.put(c, pos);   
                    dfa.put(count, aux2);
                }
                count++;
                aux2 = new HashMap();
                aux3 = new HashSet();
            }
            
            return dfa;
        }
        public static int verificaSeJaExiste(HashMap<Integer, HashSet<Integer>> dfa, HashSet<Integer> aux )
        {

            aux.toArray();
            
            for(int i=0;i<dfa.size();i++)
            {
                    if(dfa.get(i).equals(aux))
                       return i;
            }
            
            return dfa.size();
        }
        
}