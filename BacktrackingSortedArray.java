public class BacktrackingSortedArray implements Array<Integer>, Backtrack {

    private Stack stack;
    private int[] arr;
    private int nonEmptyCellsNumber = 0;



    // Do not change the constructor's signature
    public BacktrackingSortedArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
    }
    
    @Override
    public Integer get(int index) {
    	if(index >= nonEmptyCellsNumber | index < 0)
    		return -1;
    	return arr[index];
    }

    @Override
    public Integer search(int x) {
        //classic binary search
    	int output = -1;   // default (not found) value
        boolean found = false;
        int low = 0, high = nonEmptyCellsNumber - 1;
        while (low <= high & !found) {
            int middle = (low+high)/2;
            if(arr[middle] == x) {
                output = middle;
                found = true;
            }
            else 
            	if (x < arr[middle])
            		high = middle-1;
            	else 
            		low = middle+1;
        }	
        return output;
    }

    @Override
    public void insert(Integer x) {
        int closest_index = findClosest(x);
        shift_right(closest_index);
        stack.push(new ArrInsertData(x,closest_index));
        arr[closest_index] = x;
        nonEmptyCellsNumber++;
    }

    private int findClosest(int target) {
        int n = nonEmptyCellsNumber;

        if(n==0)
            return 0;
        if (target <= arr[0])
            return 0;
        if (target >= arr[n - 1])
            return n;

        int i = 0, j = n, mid = 0;
        while (i < j) {
            mid = (i + j) / 2;

            if (target < arr[mid]) {

                if (mid > 0 && target > arr[mid - 1])
                    return mid;
                j = mid;
            }

            else {
                if (mid < n-1 && target < arr[mid + 1])
                    return mid + 1;
                i = mid + 1;// update i
            }
        }
        return mid;
    }

    private void shift_right(int starting_index) {
        for(int i=nonEmptyCellsNumber-1;i>=starting_index;i--)
        {
            arr[i+1] = arr[i];
        }
    }

    @Override
    public void delete(Integer index) {
    	if(index < nonEmptyCellsNumber | index >= 0) {
    		stack.push(new ArrDeleteData(arr[index],index));
    		_delete(index);
    	}
    }

    private void _delete(Integer index) {
        shift_left(index);
        nonEmptyCellsNumber--;
    }


    private void shift_left(int starting_index) {
        for(int i=starting_index;i<nonEmptyCellsNumber;i++)
            arr[i] = arr[i+1];
    }

    @Override
    public Integer minimum() {
    	if (nonEmptyCellsNumber == 0)
    		return -1;
        return 0;
    }

    @Override
    public Integer maximum() {
        return nonEmptyCellsNumber-1;
    }

    @Override
    public Integer successor(Integer index) {
    	if(index >= nonEmptyCellsNumber-1 | index < 0)
    		return -1;
        return index+1;
    }

    @Override
    public Integer predecessor(Integer index) {
    	if(index >= nonEmptyCellsNumber | index <= 0)
    		return -1;
        return index-1;
    }

    @Override
    public void backtrack() {
    	
    	if (!stack.isEmpty()) {
    		System.out.println("backtracking performed");
    		Object obj = stack.pop();
    		if(obj instanceof ArrInsertData) {
    			_backtrack_insert((ArrInsertData)obj);
    		}
    		if(obj instanceof ArrDeleteData){
            _backtrack_delete((ArrDeleteData)obj);
    		}
    	}
    }

    private void _backtrack_delete(ArrDeleteData delData) {
        shift_right(delData.index);
        arr[delData.index] = delData.data;
        nonEmptyCellsNumber++;
    }

    private void _backtrack_insert(ArrInsertData insData) {
        shift_left(insData.index);
        nonEmptyCellsNumber--;
    }

    @Override
    public void retrack() {
        // Do not implement anything here!!
    }

    @Override
    public void print() {
        for(int i=0;i<nonEmptyCellsNumber-1;i++)
            System.out.print(arr[i] + " ");
        if(nonEmptyCellsNumber!=0)
            System.out.print(arr[nonEmptyCellsNumber-1]);
    }
}