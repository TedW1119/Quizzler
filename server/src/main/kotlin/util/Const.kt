package util

// if there is a better way of doing this plz do
object Constants {
    const val MONGO_URI = "mongodb+srv://abnormally:distributed@abnormally-distributed.naumhbd.mongodb.net/?retryWrites=true&w=majority"
    const val MONGO_DB = "abnormally-distributed"
    const val QUIZ_COLLECTION = "quizzes"
    const val QUESTION_COLLECTION = "questions"
    const val ACCOUNT_COLLECTION = "accounts"
    const val SAMPLE_TEXT = "Operating Systems - CS350 Guide\n" +
            "\n" +
            "\n" +
            "Unit 1 | Introduction, Background and Overview:\n" +
            "\n" +
            "\n" +
            "What is an operating system?\n" +
            "* The Operating System: A layer between applications and hardware (middleman) which is in charge of making sure the system operates correctly and efficiently in an easy-to-use manner:\n" +
            "   * The primary way the OS does this is through a general technique that we call virtualization. That is, the OS takes a physical resource (such as the processor, or memory, or a disk) and transforms it into a more general, powerful, and easy-to-use virtual form of itself. Thus, we sometimes refer to the operating system as a virtual machine.\n" +
            "* Correctness/Resource Manager: Prevents memory from being accessed when not supposed to\n" +
            "   * Hides details and hardware\n" +
            "* Protection: Prevents one process/user from clobbering another\n" +
            "* An OS is also an library of Standard Services (Standard Library)\n" +
            "\n" +
            "\n" +
            "Introduction to Multitasking:\n" +
            "* Collaborating multitasking will fail if the process never yields (infinite loop) or is scribbled over other processes' memory to make them fail. Two methods to fix:\n" +
            "   * Preemption: Take CPU away from looping process (kick threads off the core)\n" +
            "   * Memory Protection: Protects process’s memory from one another\n" +
            "\n" +
            "\n" +
            "Introduction to Protection:\n" +
            "* Preemption: Kick threads off the core if there is a hog\n" +
            "* Interposition/Mediation: Processes should only be trying to access hardware through the OS\n" +
            "* Privileged and Unprivileged Modes in CPUs\n" +
            "   * Unprivileged USER Mode: Applications are unprivileged (must ask OS to access certain hardware)\n" +
            "   * Privileged Supervisor/KERNEL Mode: OS is privileged (protection operations can only be done in privileged mode)\n" +
            "      * The OS Kernel always runs in privileged mode (creates/deletes processes and provides access to hardware)\n" +
            "\n" +
            "\n" +
            "Unit 2 | Processes, The Kernel, and System Calls\n" +
            "\n" +
            "\n" +
            "Unit 2A | The Process:\n" +
            "\n" +
            "\n" +
            "The Process:\n" +
            "* Process: A process is an instance of a running program\n" +
            "* Time Sharing: The OS creates this illusion by virtualizing the CPU. By running one process, then stopping it and running another, and so forth, the OS can promote the illusion that many virtual CPUs exist when in fact there is only one physical CPU (or a few).\n" +
            "* The OS contains a scheduling policy to determine what process runs\n" +
            "\n" +
            "\n" +
            "The Process API:\n" +
            "* Create: Create new processes\n" +
            "* Destroy: Destroy processes forcefully (many processes exit when complete)\n" +
            "* Wait: Wait for process to stop running\n" +
            "* Miscellaneous Control: Suspend a process for a while and then resume it later\n" +
            "* Status: Status on the information about the process (length and state)\n" +
            "\n" +
            "\n" +
            "Process Creation:\n" +
            "* To create a process, it must load its code from a disk (SSD) into RAM\n" +
            "* Then, a runtime stack should be created and maybe some memory for the program’s heap\n" +
            "* Initialize input-output (file descriptors) ⇒ 3: Standard Input, Output and Error\n" +
            "* Each process has its own view of machine (own address space, open file, and virtual CPU)\n" +
            "\n" +
            "\n" +
            "Process States:\n" +
            "* Running: In the running state, a process is running on a processor. This means it is executing instructions.\n" +
            "* Ready: In the ready state, a process is ready to run but for some reason the OS has chosen not to run it at this given moment.\n" +
            "* Blocked: In the blocked state, a process has performed some kind of operation that makes it not ready to run until some other event takes place. A common example: when a process initiates an I/O request to a disk, it becomes blocked and thus some other process can use the processor."
}
