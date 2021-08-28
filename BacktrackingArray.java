public class BacktrackingArray implements Array<Integer>, Backtrack {
    private Stack stack;
    private int[] arr;
    private int nonEmptyCellsNumber = 0;


    // Do not change the constructor's signature
    public BacktrackingArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        nonEmptyCellsNumber = 0;

    }

    @Override
    public Integer get(int index) {
    	if(index >= nonEmptyCellsNumber | index < 0)
            return -1;
        return arr[index];
    }

    @Override
    public Integer search(int x) {
        for (int i=0;i<nonEmptyCellsNumber;i++) {
            if (x == arr[i])
                return i;
        }
        return -1;
    }

    @Override
    public void insert(Integer x) {
        stack.push(new ArrInsertData());
        _insert(x);
    }

    private void _insert(Integer x) {
        arr[nonEmptyCellsNumber] = x;
        nonEmptyCellsNumber++;
    }

    @Override
    public void delete(Integer index) {
    	if(index < nonEmptyCellsNumber | index >= 0) {
    		stack.push(new ArrDeleteData(arr[index],index));
    		arr[index] = arr[nonEmptyCellsNumber-1];
    		nonEmptyCellsNumber--;
    	}
    }

    @Override
    public Integer minimum() {
    	if (nonEmptyCellsNumber == 0)
    		return -1;
        int min_index = 0;
        for(int i=1;i<nonEmptyCellsNumber;i++)
            if (arr[min_index]>arr[i])
                min_index = i;
        return  min_index;
    }

    @Override
    public Integer maximum() {
    	if (nonEmptyCellsNumber == 0)
    		return -1;
        int max_index = 0;
        for(int i=1;i<nonEmptyCellsNumber;i++)
            if(arr[max_index]<arr[i])
                max_index = i;
        return  max_index;
    }

    @Override
    public Integer successor(Integer index) {
        int successor_index = -1;

        for(int i=0;i<arr.length;i++)
        {
            if(arr[i]>arr[index])
            {
               if(successor_index ==-1)
                   successor_index = i;
               else
                   if(arr[i]<arr[successor_index])
                       successor_index=i;
            }
        }
        return successor_index;
    }

    @Override
    public Integer predecessor(Integer index) {
        int predecessor_index = -1;

        for(int i=0;i<arr.length;i++)
        {
            if(arr[i]<arr[index])
            {
                if(predecessor_index ==-1)
                    predecessor_index = i;
                else
                if(arr[i]>arr[predecessor_index])
                    predecessor_index=i;
            }
        }
        return predecessor_index;
    }


    @Override
    public void backtrack() {
    	
    	if (!stack.isEmpty()) {
    		System.out.println("backtracking performed");
    		Object obj = stack.pop();
    		if(obj instanceof ArrInsertData)
    			nonEmptyCellsNumber--;
    		if(obj instanceof ArrDeleteData)
    		{
    			ArrDeleteData deleteData = (ArrDeleteData)obj;
    			_insert(arr[deleteData.index]);
    			arr[deleteData.index] = deleteData.data;
    		}
    	}
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

class ArrInsertData {

    public int data;
    public int index;

    public ArrInsertData(int data,int index){
        this.data = data;
        this.index = index;
    }

    public ArrInsertData() { }
}

class ArrDeleteData {

    public int data;
    public int index;

    public ArrDeleteData(int data ,int index) {
        this.data = data;
        this.index = index;
    }

}