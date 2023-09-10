---
title: Developer Documentation
layout: default
nav_order: 3
has_children: true
has_toc: false
---

# Developer Documentation
{: .no_toc}

This chapter will present the implementation of the HexArc framework in detail, in order
to allow anyone who's interested to contribute to the project.

Before reading this chapter, it is suggested to gain familiarity with the framework by
reading the [User Documentation](/hexarc/user-documentation), which showcases the main
functionalities provided by HexArc.

## Table of Contents
{: .no_toc}

- TOC
{:toc}

---

## Modules

The HexArc framework is divided in independent **modules**, each adding different functionalities
to the framework. 

A proper module should not have any dependencies on the other modules of the framework. In fact,
it should be considered as a standalone project, so much so that in the future it may even be
maintained as an extension of the framework outside the core infrastructure of HexArc. The same
applies for the documentation of the modules.

Here's a list of the main modules exposed by the HexArc framework:
- [Architecture Module](/hexarc/developer-documentation/0-architecture)
- [Persistence Module](/hexarc/developer-documentation/1-persistence)