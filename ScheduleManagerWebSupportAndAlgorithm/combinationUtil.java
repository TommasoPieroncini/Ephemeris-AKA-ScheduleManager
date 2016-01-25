import java.io.*;
import java.util.ArrayList;

class combinationUtil {

    /* arr[]  ---> Input Array
    data[] ---> Temporary array to store current combination
    start & end ---> Staring and Ending indexes in arr[]
    index  ---> Current index in data[]
    r ---> Size of a combination to be printed */
    public ArrayList<Course[]> combinationUtils(Course arr[], Course data[], int start,
                                int end, int index, int r)
    {
        ArrayList<Course[]> list = new ArrayList<Course[]>();
        Course[] list2 = new Course[r];

        // Current combination is ready to be printed, print it
        if (index == r)
        {
            for (int j=0; j<r; j++) {
                list2[j] = data[j];
            }
            list.add(list2);
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            data[index] = arr[i];
            combinationUtils(arr, data, i+1, end, index+1, r);
        }

        return list;
    }

    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    public void printCombination(Course arr[], int n, int r)
    {
        // A temporary array to store all combination one by one
        Course data[] = new Course[r];

        // Print all combination using temprary array 'data[]'
        combinationUtils(arr, data, 0, n-1, 0, r);
    }
}