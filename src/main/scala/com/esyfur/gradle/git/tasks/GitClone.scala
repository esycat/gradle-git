package com.esyfur.gradle.git.tasks

import scala.sys.process._
import scala.collection.mutable.ListBuffer

import org.gradle.api.tasks.TaskAction
import org.gradle.api.GradleException

import org.ajoberstar.gradle.git.tasks.{GitClone => JGitClone}

class GitClone extends JGitClone {

    final val action = "clone"

    private var depth: Int = 0
    private var recursive: Boolean = false

    @TaskAction
    override def cloneRepo(): Unit = {
        val retVal = command.!

        if (retVal != 0) {
            val errMsg = "git-%s exited with non-zero status (%d)."
            throw new GradleException(errMsg.format(action, retVal))
        }
    }

    protected def command: Seq[String] = {
        val cmd = ListBuffer("git", action)

        if (!getCheckout())      cmd += ("--no-checkout")
        if (getBare())           cmd += ("--bare")
        if (getRemote() != null) cmd += ("--origin", getRemote())
        if (getDepth() > 0)      cmd += ("--depth", getDepth().toString())
        if (getRecursive())      cmd += ("--recursive")

        if (getBranch() != null)   cmd += ("--branch", getBranch())
        else if (getTag() != null) cmd += ("--branch", getTag())

        cmd += getUri()
        cmd += getDestinationDir().toString()

        cmd
    }

    def setDepth(depth: Int): Unit = {
        if (depth < 0) throw new IllegalArgumentException("Depth cannot be negative.")

        this.depth = depth
    }

    def getDepth(): Int = {
        depth
    }

    def setRecursive(recursive: Boolean): Unit = {
        this.recursive = recursive
    }

    def getRecursive(): Boolean = {
        recursive
    }

}
