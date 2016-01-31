import java.util.ArrayList;


public class Combination {

    public static ArrayList<int[]> thelist = new ArrayList<int[]>();

    public static void combination(int N, int K){

        // get the length of the array
        // e.g. for {'A','B','C','D'} => N = 4

        if(K > N){
            System.out.println("Invalid input, K > N");
            return;
        }
        // calculate the possible combinations
        // e.g. c(4,2)
        //c(N,K);

        // get the combination by index
        // e.g. 01 --> AB , 23 --> CD
        int combination[] = new int[K];

        // position of current index
        //  if (r = 1)              r*
        //  index ==>        0   |   1   |   2
        //  element ==>      A   |   B   |   C
        int r = 0;
        int index = 0;

        while(r >= 0){
            // possible indexes for 1st position "r=0" are "0,1,2" --> "A,B,C"
            // possible indexes for 2nd position "r=1" are "1,2,3" --> "B,C,D"

            // for r = 0 ==> index < (4+ (0 - 2)) = 2
            if(index <= (N + (r - K))){
                combination[r] = index;

                // if we are at the last position print and increase the index
                if(r == K-1){

                    //do something with the combination e.g. add to list or print
                    int[] combi = new int[K];
                    for (int j = 0; j < combi.length; j++) {
                        combi[j] = combination[j];
                    }
                    thelist.add(combi);
                    index++;
                }
                else{
                    // select index for next position
                    index = combination[r]+1;
                    r++;
                }
            }
            else{
                r--;
                if(r > 0)
                    index = combination[r]+1;
                else
                    index = combination[0]+1;
            }
        }
    }
}