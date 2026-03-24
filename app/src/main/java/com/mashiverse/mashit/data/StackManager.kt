package com.mashiverse.mashit.data

class StackManager<T> {
    private val undoStack = ArrayDeque<T>()
    private val redoStack = ArrayDeque<T>()

    fun record(currentState: T) {
        undoStack.addLast(currentState)
        if (undoStack.size > STACK_SIZE) {
            undoStack.removeFirst()
        }
        redoStack.clear()
    }

    fun undo(currentState: T): T? {
        if (undoStack.isEmpty()) return null
        redoStack.addLast(currentState)
        return undoStack.removeLast()
    }

    fun redo(currentState: T): T? {
        if (redoStack.isEmpty()) return null
        undoStack.addLast(currentState)
        return redoStack.removeLast()
    }

    fun clear() {
        undoStack.clear()
        redoStack.clear()
    }

    val canUndo: Boolean get() = undoStack.isNotEmpty()
    val canRedo: Boolean get() = redoStack.isNotEmpty()
    
    companion object {
        const val STACK_SIZE = 10
    }
}