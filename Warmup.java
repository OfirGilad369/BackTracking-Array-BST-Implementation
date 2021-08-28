public class Warmup {



    public static int backtrackingSearch(int[] arr, int x, int fd, int bk, Stack myStack) {

        int i=0,curr_fd=fd;
        while(i<arr.length) {
            if(arr[i]==x)
                return i;
            if(curr_fd==0){
                curr_fd=fd;
                i = (int)myStack.pop(); // going back bk  steps
            }
            else
            {
                curr_fd--;
                i++;
                if(fd - curr_fd== fd-bk) // check if fd-bk steps were performed
                    myStack.push(i);
            }
        }
        return -1;


   }

    public static int consistentBinSearch(int[] arr, int x, Stack stack) {

        int num_of_steps_back=0;
        BinSerchData last_step;
        int l = 0, r = arr.length - 1,m;
        while (l <= r) {
            num_of_steps_back = isConsistent(arr);
            if (num_of_steps_back >0)
            {
                for(int i=0;!stack.isEmpty()&&i<num_of_steps_back;i++)
                {
                    last_step = (BinSerchData)stack.pop();
                    l= last_step.l;
                    r = last_step.r;
                }
            }
            else {
                stack.push(new BinSerchData(l,r));
            }
                m = l + (r - l) / 2;
                if (arr[m] == x)
                    return m;
                if (arr[m] < x)
                    l = m + 1;
                else
                    r = m - 1;
        }
        return -1;
    }



    private static int isConsistent(int[] arr) {
        double res = Math.random() * 100 - 75;

        if (res > 0) {
            return (int)Math.round(res / 10);
        } else {
            return 0;
        }
    }
}

class BinSerchData {

    public int l;
    public int r;

    public BinSerchData(int l ,int r)
    {
        this.l = l;
        this.r = r;
    }

}


